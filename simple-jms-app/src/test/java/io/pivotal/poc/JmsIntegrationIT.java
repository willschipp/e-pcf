package io.pivotal.poc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.SocketUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:/META-INF/spring/integration/jms-context.xml","classpath:/META-INF/spring/integration/client-context.xml"})
@ActiveProfiles("junit")
public class JmsIntegrationIT {
	
	@Value("${directory}")
	private String directoryLocation;	
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Autowired
	private Queue queue;
	
	@Autowired
	private Topic topic;
	
	static BrokerService broker;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		//setup and bind jndi here
		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		//build a JMS server
		int port = SocketUtils.findAvailableTcpPort();
		broker = new BrokerService();
		broker.addConnector("tcp://localhost:" + port);
		broker.setBrokerName("test_broker");
		broker.setUseJmx(false);
		broker.setUseShutdownHook(true);
		broker.setPersistent(false);
		broker.setDeleteAllMessagesOnStartup(true);
		broker.start();
		//set the connection
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:" + port);
		PooledConnectionFactory connectionFactory = new PooledConnectionFactory(factory);
		//bind to jndi
		builder.bind("jms/connectionFactory", connectionFactory);
		//build a queue
		ActiveMQQueue queue = new ActiveMQQueue("outbound.queue");
		
		//bind
		builder.bind("jms/outbound.queue",queue);
		//bind the topic
		ActiveMQTopic topic = new ActiveMQTopic("inbound.topic");
		//bind
		builder.bind("jms/inbound.topic", topic);
	}
	
	
	//now test just the jms part
	@Test
	public void testJMS() {
		new JmsTemplate(connectionFactory).convertAndSend(queue, "hello world");
		//now retrieve
		String message = new JmsTemplate(connectionFactory).receiveAndConvert(queue).toString();
		//check
		assertEquals(message,"hello world");
	}
	
	//now test a pub sub
	@Test
	public void testJMSPubSub() throws Exception {
		//send a message
		JmsTemplate template = new JmsTemplate(connectionFactory);
		template.setPubSubDomain(true);
		template.convertAndSend(topic, "hello topic");
		//wait
		Thread.sleep(1000);
	}

	//now an end-to-end test
	//going to build a file with line delimited messages and see it sent through
	@Test(timeout=5000)
	public void testJMSFilePublish() throws Exception {
		//build up a message list
		List<String> messages = new ArrayList<String>();
		messages.add("hello world 0");
		messages.add("hello world 1");
		messages.add("hello world 2");		
		//write a file to the test location
		FileWriter writer = new FileWriter(new File(directoryLocation + File.separator + "test.log"));
		for (String message : messages) {
			writer.write(message);
			writer.write(System.getProperty("line.separator"));//line feed
		}//end for
		writer.flush();
		writer.close();
		//wait
		Thread.sleep(500);
		//now lets see if it's on the queue --> should be 3 messages
		for (int i=0;i<messages.size();i++) {
			String received = new JmsTemplate(connectionFactory).receiveAndConvert(queue).toString();
			assertTrue(messages.contains(received));
		}//end for
	}
	
}
