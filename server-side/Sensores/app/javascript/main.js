/**
*/
var main = (function() {
    //privado
    /*
     * Constante peticion ajax finalizada
     */
    var STATE_FINISH = 4;
    /*
     * Constante Status ok descarga
     */
    var STATUS_OK = 200;
	
    /*
    * TAG usado en logs
    */
    var TAG = "main.js";
	var URL = "proxy.php";

    /*
     * Funcion privada que inicializa todos los apis y metodos de control de elementos
     */

    function _onLoad() {
        try {
			
			carga_datos();
			var myInterval=setInterval(function(){carga_datos()},4000);
        	} catch (e) {
            alert("Failed to create XHR");
        }
        
    }
	
	
	function carga_datos(){
            var xhrObj = new XMLHttpRequest();
            xhrObj.onreadystatechange =  function _xhrReadyStateChange() {
        	try {
           	 //No usamos switch porque hay que tratar codigo y estado
            	if (STATE_FINISH === xhrObj.readyState && STATUS_OK === xhrObj.status) {
					parsea_xml(xhrObj.responseText);
            		}
        		} catch (e) {
            	alert("error tratando estados objeto xhr" + e)
        		}

    		}
            xhrObj.open("GET", URL);
            xhrObj.send(null);		
	}

	function parsea_xml(xmltext){
		var name,contextValue,entityId;
		var namevector=[];
		var contextValuevector=[];
		var xmlDoc = $.parseXML(xmltext);
		$xml = $(xmlDoc);
		var contextElementResponse=xmlDoc.getElementsByTagName("contextElementResponse");
		for (var i=0;i<contextElementResponse.length;i++){
			namevector=[];
			contextValuevector=[];
			var context=contextElementResponse[i].getElementsByTagName("contextElement")[0];
			var entityId=context.getElementsByTagName("entityId")[0].childNodes[1].textContent;
			var contextAttributeList=context.getElementsByTagName("contextAttributeList")[0];
			var contextAttribute=contextAttributeList.getElementsByTagName("contextAttribute");
			for (var j=0;j<contextAttribute.length;j++){
				var name=contextAttribute[j].childNodes[1].textContent;
				var contextValue=contextAttribute[j].childNodes[5].textContent;
				namevector.push(name);
				contextValuevector.push(contextValue);
				
			}
			_pintaSensores(namevector,contextValuevector,entityId,i);
		}
	}
	
	
	function isNumber(n){
 	 	return !isNaN(parseFloat(n)) && isFinite(n);
	}		
	
	function _pintaSensores(namevector,contextValuevector,entityId,orden){
		var div = document.createElement("div");
		var divtexto = document.createElement("div");
		if (orden==0 || orden==3)
			div.setAttribute("class","caja_dcha");
		else
			div.setAttribute("class","caja_izda");
			
		div.setAttribute("id","sensor"+orden);
		divtexto.innerHTML =divtexto.innerHTML+"<strong>"+entityId+"</strong><br>"
		for (var i=0;i<namevector.length;i++){
			if (orden==0) {
					div.style.top="-10px";
					div.style.left="-38px";
				}
			if (orden==1) {
					div.style.top="-115px";
					div.style.left="256px";
				}
			if (orden==2) {
					div.style.top="75px";
					div.style.left="260px";
				}	
			if (orden==3) {
					div.style.top="-30px";
					div.style.left="-34px";
				}				
			if (isNumber(contextValuevector[i]))	{		
				divtexto.innerHTML =divtexto.innerHTML+ namevector[i]+": "+ Math.round(contextValuevector[i] * 100) / 100 +"<br>";
			} else{
				divtexto.innerHTML =divtexto.innerHTML+ namevector[i]+": "+ contextValuevector[i];
				if (contextValuevector[i]=='QUIET'){
					divtexto.innerHTML =divtexto.innerHTML+"<img src='/Sensores/images/green_light.png' style='padding-left:20px;width:16px'>"
				} else{
					divtexto.innerHTML =divtexto.innerHTML+"<img src='/Sensores/images/red_light.png' style='padding-left:20px;width:16px'>"
				}
				divtexto.innerHTML =divtexto.innerHTML+"<br>";
			}
		}
		//Eliminamos los anteriores
		if (document.getElementById("sensor"+orden))
			document.getElementById("sensor"+orden).parentNode.removeChild(document.getElementById("sensor"+orden));		
		document.getElementById("sensores").appendChild(div);
		document.getElementById("sensor"+orden).appendChild(divtexto);	
	}
	
	


    //metodos publicos
    return {
        onLoad: function() {
            _onLoad();
        },
        onUnload: function() {
            player.deinit();
        }
    }
})();
