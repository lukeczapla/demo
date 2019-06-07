package org.frisch;

import com.google.cloud.speech.v1.*;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;
import java.util.List;

/**
 * This class uses the Google Cloud Speech-to-Text API to translate text
 * sent as a WAV file with Linear PCM codec.
 */
public class TextMaker {
    private RecognitionConfig config;
    private RecognitionAudio audio;

    /**
     * Sets up the TextMaker to translate the audio speech data.
     * @param audioBytes The bytes of the speech data to translate
     */
    public TextMaker(byte[] audioBytes) {
        config = RecognitionConfig.newBuilder()
                .setEncoding(AudioEncoding.LINEAR16)
                .setSampleRateHertz(44100)
                .setLanguageCode("en-US")
                .build();
        audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioBytes))
                .build();
    }

    public String getText() {
        try (SpeechClient speechClient = SpeechClient.create()) {

            RecognizeResponse response = speechClient.recognize(config, audio);
            System.out.println(response.getResultsCount());
            List<SpeechRecognitionResult> results = response.getResultsList();
            for (SpeechRecognitionResult r : results) {
                SpeechRecognitionAlternative alt = r.getAlternativesList().get(0);
                if (alt.getTranscript() != null) return alt.getTranscript();
            }
            return "no results found";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred";
        }
    }

}
