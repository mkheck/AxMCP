package com.example;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import org.glassfish.tyrus.client.ClientManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URI;

/**
 * Created by markheckler on 2/2/17.
 */
@Service
public class AlexaSpeechlet implements Speechlet {
    private ClientManager clientManager;
    private WsClientEndpoint endpoint;

    @Value("${websocket_uri}")
    private String wsUri;

    @Value("${application.id}")
    private String applicationId;

    public AlexaSpeechlet() {
        this.clientManager = ClientManager.createClient();
        this.endpoint = new WsClientEndpoint();
    }

    @PostConstruct
    public void init() {
        //System.out.println("--->WebSocket URI: " + wsUri);
        //System.out.println("---> Application Id: " + applicationId);
        try {
            this.clientManager.connectToServer(this.endpoint, URI.create(wsUri));
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        return null;
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        if (session.getApplication().getApplicationId().equalsIgnoreCase(applicationId)) {
            Intent intent = request.getIntent();
            if (intent == null) {
                throw new SpeechletException("Unrecognized intent!");
            }

            return processCommand(intent);
        } else {
            throw new SpeechletException("You are not authorized to access this skill. Cease & desist, and have a nice day.");
        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
    }

    private SpeechletResponse processCommand(Intent intent) {
        String cmdExec = "", cmdDesc = "";

        try {
            switch (intent.getName()) {
                case "ModeIntent":
                    if (intent.getSlot("Mode").getValue().equalsIgnoreCase("manual")) {
                        cmdExec = "0a";
                        cmdDesc = "Manual override complete";
                    } else {
                        cmdExec = "0A";
                        cmdDesc = "Automated mode engaged";
                    }
                    break;
                case "ControlIntent":
                    switch (intent.getSlot("Device").getValue().toUpperCase()) {
                        case "POWER":
                            if (intent.getSlot("OnOff").getValue().equalsIgnoreCase("on")) {
                                cmdExec = "0P";
                                cmdDesc = "Power on";
                            } else {
                                cmdExec = "0p";
                                cmdDesc = "Power off";
                            }
                            break;
                        case "STATUS LAMP":
                            if (intent.getSlot("OnOff").getValue().equalsIgnoreCase("on")) {
                                cmdExec = "0L";
                                cmdDesc = "Status lamp on";
                            } else {
                                cmdExec = "0l";
                                cmdDesc = "Status lamp off";
                            }
                            break;
                        case "INTERIOR LIGHT":
                            if (intent.getSlot("OnOff").getValue().equalsIgnoreCase("on")) {
                                cmdExec = "0I";
                                cmdDesc = "Interior light on";
                            } else {
                                cmdExec = "0i";
                                cmdDesc = "Interior light off";
                            }
                            break;
                    }
                    break;
                case "WindowIntent":
                    if (intent.getSlot("OpenClose").getValue().equalsIgnoreCase("open")) {
                        cmdExec = "0W";
                        cmdDesc = "Windows open";
                    } else {
                        cmdExec = "0w";
                        cmdDesc = "Windows closed";
                    }
                    break;
            }
            this.endpoint.getSession().getBasicRemote().sendText(cmdExec);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getSpeechletResponse(cmdDesc);
    }

    private SpeechletResponse getSpeechletResponse(String cmdDescription) {
        SimpleCard card = new SimpleCard();
        card.setTitle("Command Executed");
        card.setContent(cmdDescription);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(cmdDescription);

        return SpeechletResponse.newTellResponse(speech, card);
    }
}
