package de.mknoll.thesis.tests.di;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.annotations.Inject;
import org.picocontainer.injectors.MultiInjection;
import org.picocontainer.parameters.ConstantParameter;

import de.mknoll.thesis.datastructures.graph.writer.EdgeListWriter;
import de.mknoll.thesis.framework.logger.ConsoleLogger;
import de.mknoll.thesis.framework.logger.LoggerInterface;
import de.mknoll.thesis.tests.testclasses.*;



/**
 * Testcase implements some tests for checking out functionality of 
 * Pico DI container.
 * 
 * @author Michael Knoll <mimi@kaktusteam.de>
 */
public class DependencyInjectionTest {
	
	@Test
	public void diContainerWithMultipleInjectionInjectsViaFieldConstructorAndSetter() {
		A a = new A();
		assertTrue(a.getClass() == A.class);
		
		MutablePicoContainer container = new PicoBuilder()
			.withCaching()
			.withComponentFactory(new MultiInjection())
			.build();
		assertTrue(container.getClass() == DefaultPicoContainer.class);
		
		container.addComponent(A.class);
		container.addComponent(B.class);
		container.addComponent(C.class);
		container.addComponent(D.class);
		
		a = container.getComponent(A.class);
		assertTrue(a.getClass() == A.class);
		
		D d = container.getComponent(D.class);
		assertTrue(d.getClass() == D.class);
		assertTrue(d.getA().getClass() == A.class);
		assertTrue(d.getB().getClass() == B.class);
		assertTrue(d.getC().getClass() == C.class);
	}
	
}
