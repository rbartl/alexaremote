package com.edvbartl.alexafb.services

import com.amazon.speech.speechlet.IntentRequest
import com.amazon.speech.speechlet.LaunchRequest
import com.amazon.speech.speechlet.Session
import com.amazon.speech.speechlet.SessionEndedRequest
import com.amazon.speech.speechlet.SessionStartedRequest
import com.amazon.speech.speechlet.SpeechletException
import com.amazon.speech.speechlet.SpeechletResponse
import com.amazon.speech.ui.LinkAccountCard
import com.amazon.speech.ui.PlainTextOutputSpeech
import com.amazon.speech.ui.Reprompt
import com.amazon.speech.ui.SimpleCard
import com.amazon.speech.speechlet.Context
import com.amazon.speech.speechlet.Speechlet

import org.slf4j.Logger;
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service;

@Service
class AlexaFbSpeechlet implements Speechlet {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MqttService mqttService


    def commandMap = [
            lauter:'volume-up',
            leiser:'volume-down',
            "nach oben":'direction-up',
            "nach unten":'direction-down',
            "rauf":'direction-up',
            "runter":'direction-down',
            oben:'direction-up',
            unten:'direction-down',
            "nach rechts":'direction-right',
            "nach links":'direction-left',
            rechts:'direction-right',
            links:'direction-left',
            ausschalten:'activities/poweroff/command.off',
            pause:'pause',
            play:'play',
            weiter:'play',
            spiel:'play',
            spielab: 'play',
            ok:'select',
            off:'activities/poweroff/command.off',
            eins:'1',
            zwei:'2',
            drei:'3',
            vier:'4',
            fuenf:'5',
            sechs:'6',
            sex:'6',
            sieben:'7',
            acht:'8',
            neun:'9',
            null:'0',
            zero:'0',
            raus: 'exit',
            beenden: 'exit',
            exit:'exit',
            zurueck:'back',
            "zurück": "back",
            "menü": "menu",
            "menü öffnen": "menu",
            "stop": "stop",
            "rot": "red",
            "grün": "green",
            "blau": "blue",
            "gelb": "yellow",
            fernsehen: 'activities/watch-tv/command.on',
            "fernsehen einschalten": 'activities/watch-tv/command.on',
            "fernseher einschalten": 'activities/watch-tv/command.on',
            "fire tv sehen": 'activities/fire-tv/command.on',
            "fire tv schauen": 'activities/fire-tv/command.on',
            "fire tv einschalten": 'activities/fire-tv/command.on',
            "fire t. v. einschalten": 'activities/fire-tv/command.on',
    ]

    /**
     * This is called when the session is started
     * Add an initialization setup for the session here
     * @param request SessionStartedRequest
     * @param session Session
     * @throws SpeechletException
     */
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())

    }

    /**
     * This is called when the skill/speechlet is launched on Alexa
     * @param request LaunchRequest
     * @param session Session
     * @return
     * @throws SpeechletException
     */
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())

        return getWelcomeResponse()
    }

    /**
     * This is the method fired when an intent is called
     *
     * @param request IntentRequest intent called from Alexa
     * @param session Session
     * @return SpeechletResponse tell or ask type
     * @throws SpeechletException
     */
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())

        log.info("request:" + request.getIntent())
        log.info("slots:" + request.getIntent().slots)
        if ("MultiCommandoIntent".equals(request.getIntent().name)||
                "CommandoIntent".equals(request.getIntent().name)) {
            if (request.getIntent().slots) for (def slot : request.getIntent().slots) {
                log.info("slot value:"  + slot.value.value )
                if (commandMap.containsKey(slot.value.value)) {
                    def command = commandMap[slot.value.value]
                    def channel = "command"
                    if (command.contains(".")) {
                        channel = command.split("\\.")[0]
                        command = command.split("\\.")[1]
                    }
                    mqttService.publish(channel, command)
                }
            }
        }
        if ("DuplicateCommandoIntent".equals(request.getIntent().name)) {
            String samount
            int amount = 0
            String slotcommand
            if (request.getIntent().slots) for (def slot : request.getIntent().slots) {
                if ("amount" == slot.key) {
                    samount = slot.value.value
                    amount = Integer.parseInt(samount)
                }
                if ("command" == slot.key) {
                    slotcommand = slot.value.value
                }
            }
            if (commandMap.containsKey(slotcommand)) {
                def command = commandMap[slotcommand]
                def channel = "command"
                if (command.contains(".")) {
                    channel = command.split("\\.")[0]
                    command = command.split("\\.")[1]
                }
                amount.times({
                    mqttService.publish(channel, command)
                })
            }
        }



        PlainTextOutputSpeech speech = new PlainTextOutputSpeech()
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"Twitter Search Results")
        def speechText = "OK"
        def cardText = "I will print something"
        // Create the plain text output.
        speech.setText(speechText)
        card.setContent(cardText)
        SpeechletResponse.newTellResponse(speech, card)

    }

    /**
     * this is where you do session cleanup
     * @param request SessionEndedRequest
     * @param session
     * @throws SpeechletException
     */
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId())
        // any cleanup logic goes here
    }

    SpeechletResponse getWelcomeResponse()  {
        String speechText = "Say something when the skill starts"

        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title: "YourWelcomeCardTitle", content: speechText)

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)

        // Create reprompt
        Reprompt reprompt = new Reprompt(outputSpeech: speech)

        SpeechletResponse.newAskResponse(speech, reprompt, card)
    }

    /**
     * default responder when a help intent is launched on how to use your speechlet
     * @return
     */
    SpeechletResponse getHelpResponse() {
        String speechText = "Say something when the skill need help"
        // Create the Simple card content.
        SimpleCard card = new SimpleCard(title:"YourHelpCardTitle",
                content:speechText)
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
        // Create reprompt
        Reprompt reprompt = new Reprompt(outputSpeech: speech)
        SpeechletResponse.newAskResponse(speech, reprompt, card)
    }

    /**
     * if you are using account linking, this is used to send a card with a link to your app to get started
     * @param session
     * @return
     */
    SpeechletResponse createLinkCard(Session session) {

        String speechText = "Please use the alexa app to link account."
        // Create the Simple card content.
        LinkAccountCard card = new LinkAccountCard()
        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech(text:speechText)
        log.debug("Session ID=${session.sessionId}")
        // Create reprompt
        Reprompt reprompt = new Reprompt(outputSpeech: speech)
        SpeechletResponse.newTellResponse(speech, card)
    }

}

