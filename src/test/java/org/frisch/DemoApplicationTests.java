package org.frisch;

import com.google.api.gax.rpc.ClientStream;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.sampled.*;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() throws Exception {
		TargetDataLine line;
		AudioInputStream audio = null;

		AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info)) {
			System.out.println("Could not support format");
			throw new Exception("FAILED");
		}
		line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();
		audio = new AudioInputStream(line);
		try (SpeechClient client = SpeechClient.create()) {

			ResponseObserver<StreamingRecognizeResponse> responseObserver =
					new ResponseObserver<StreamingRecognizeResponse>() {

						public void onStart(StreamController controller) {
							// do nothing
						}

						public void onResponse(StreamingRecognizeResponse response) {
							System.out.println(response);
						}

						public void onComplete() {}

						public void onError(Throwable t) {
							System.out.println(t);
						}
					};

			ClientStream<StreamingRecognizeRequest> clientStream =
					client.streamingRecognizeCallable().splitCall(responseObserver);

			RecognitionConfig recConfig =
					RecognitionConfig.newBuilder()
							.setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
							.setLanguageCode("en-US")
							.setSampleRateHertz(16000)
							.build();
			StreamingRecognitionConfig config =
					StreamingRecognitionConfig.newBuilder().setConfig(recConfig).build();

			StreamingRecognizeRequest request =
					StreamingRecognizeRequest.newBuilder()
							.setStreamingConfig(config)
							.build(); // The first request in a streaming call has to be a config

			clientStream.send(request);

			while (true) {
				byte[] data = new byte[10];
				try {
					audio.read(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
				request =
						StreamingRecognizeRequest.newBuilder()
								.setAudioContent(ByteString.copyFrom(data))
								.build();
				clientStream.send(request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
