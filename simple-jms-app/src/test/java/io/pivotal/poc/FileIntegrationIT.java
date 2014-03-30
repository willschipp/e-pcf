package io.pivotal.poc;

import java.io.File;
import java.io.FileWriter;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/META-INF/spring/integration/file-context.xml"})
@ActiveProfiles("junit")
public class FileIntegrationIT {

	@Autowired
	@Qualifier("outbound.channel")
	private DirectChannel outboundChannel;
	
	@Value("${directory}")
	private String directoryLocation;
	
	@After
	public void after() {
		new File(directoryLocation + File.separator + "test.log").deleteOnExit();
	}
	
	@Test
	public void test() throws Exception {
		//setup
		Handler handler = new Handler();
		outboundChannel.subscribe(handler);
		//write a file to the test location
		FileWriter writer = new FileWriter(new File(directoryLocation + File.separator + "test.log"));
		for (int i=0;i<3;i++) {
			writer.write("hello world " + i);
			writer.write(System.getProperty("line.separator"));//line feed
		}//end for
		writer.flush();
		writer.close();
		//watch it come out as individual messages
		while (handler.message == null) {
			Thread.sleep(1000);
		}//end while
		System.out.println(handler.message.getPayload().toString());
	}
	
	class Handler implements MessageHandler {

		Message<?> message;
		
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			this.message = message;
		}
		
	}
	
}
