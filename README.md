MagicBox Team - FI-WARE demostrator
===================================

The MagicBox Team developed a demonstrator to test how to integrate
the FI-WARE information sources on a TV screen. This demonstrator
shows how to display a general sensor information (from a 4IN1 sensors
placed on Magdalena Palace) in a overlay widget over the TV signal
(the user can shows and hide the information). Also, in a more
specific way, the demonstrator shows how to integrate and use the 4IN1
sensors source as a security / presence detection system, with an
interface adapted to be used over the TV signal.

The demonstrator approach shows how to integrate the FI-WARE platform
on TV in two ways:

 - the first one delegate the FI-WARE communication to and external
   server that generate the information as a web page (deployed on the
   FI-WARE could infrastructure) The TV renders the external web-page as
   an overlay widget. (server-side folder)

  - the second approach implements a native client on Android for the
    FI-WARE platform and renders the information as native Android
    components as a widgets over the TV signal. (tv-widget folder)

The demonstrator don't just test the FI-WARE platform as a data
provider, also use the FI-WARE platform to send signals to a IR
emitter to turn on/off a TV or send signals to change the colour of a
LED bar.
