package org.apache.velocity.test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class HelloVelocity {

	public static void main(String[] args) {

		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

		engine.init();

		Template template = engine.getTemplate("helloVelocity.vm");

		VelocityContext ctx = new VelocityContext();
		ctx.put("name", "velocity");
		ctx.put("date", new Date().toString());
		List<String> temp = new ArrayList<String>();
		temp.add("1");
		temp.add("2");
		ctx.put("list", temp);

		StringWriter sw = new StringWriter();

		template.merge(ctx, sw);

		System.out.println(sw.toString());

	}

}
