package com.opensymphony.sitemesh3.html.rules.decorator;

import com.opensymphony.sitemesh3.InMemoryContentProperty;
import com.opensymphony.sitemesh3.SiteMeshContextStub;
import com.opensymphony.sitemesh3.ContentProperty;
import com.opensymphony.sitemesh3.tagprocessor.TagProcessor;
import junit.framework.TestCase;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * @author Joe Walnes
 */
public class SiteMeshWriteRuleTest extends TestCase {

    public void testWritesTheProperty() throws IOException {
        ContentProperty contentProperty = new InMemoryContentProperty();
        contentProperty.getChild("foo").setValue("This is the <foo> property.");
        contentProperty.getChild("bar.x").setValue("BAR");
        SiteMeshContextStub context = new SiteMeshContextStub();
        context.setContentToMerge(contentProperty);

        String in = "Hello <sitemesh:write property='foo'/> <sitemesh:write property='bar.x'/>!";
        TagProcessor tagProcessor = new TagProcessor(CharBuffer.wrap(in));
        tagProcessor.addRule("sitemesh:write", new SiteMeshWriteRule(context));
        tagProcessor.process();
        CharSequence out = tagProcessor.getDefaultBufferContents();

        assertEquals("Hello This is the <foo> property. BAR!", out.toString());
    }

    public void testRemovesTagContents() throws IOException {
        ContentProperty contentProperty = new InMemoryContentProperty();
        SiteMeshContextStub context = new SiteMeshContextStub();
        context.setContentToMerge(contentProperty);

        String in = "Hello <sitemesh:write property='notfound'/> <sitemesh:write property='found.not'/>!";
        TagProcessor tagProcessor = new TagProcessor(CharBuffer.wrap(in));
        tagProcessor.addRule("sitemesh:write", new SiteMeshWriteRule(context));
        tagProcessor.process();
        CharSequence out = tagProcessor.getDefaultBufferContents();

        assertEquals("Hello  !", out.toString());
    }

    public void testSkipsMissingProperties() throws IOException {
        ContentProperty contentProperty = new InMemoryContentProperty();
        SiteMeshContextStub context = new SiteMeshContextStub();
        contentProperty.getChild("found").setValue("FOUND");
        context.setContentToMerge(contentProperty);

        String in = "Hello <sitemesh:write property='found'>BAD</sitemesh:write>" +
                " <sitemesh:write property='notfound'>BAD</sitemesh:write>!";
        TagProcessor tagProcessor = new TagProcessor(CharBuffer.wrap(in));
        tagProcessor.addRule("sitemesh:write", new SiteMeshWriteRule(context));
        tagProcessor.process();
        CharSequence out = tagProcessor.getDefaultBufferContents();

        assertEquals("Hello FOUND !", out.toString());
    }

}
