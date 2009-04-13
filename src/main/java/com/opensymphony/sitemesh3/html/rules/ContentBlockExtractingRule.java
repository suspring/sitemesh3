package com.opensymphony.sitemesh3.html.rules;

import com.opensymphony.sitemesh3.tagprocessor.Tag;
import com.opensymphony.sitemesh3.tagprocessor.BasicBlockRule;
import com.opensymphony.sitemesh3.Content;

import java.io.IOException;

/**
 * Extracts the contents of any elements that look like
 * <code>&lt;content tag='foo'&gt;...&lt;/content&gt;</code> and write the contents
 * to a page property (page.foo).
 *
 * <p>This is a cheap and cheerful mechanism for embedding multiple components in a
 * page that can be used in different places in decorators.</p>
 *
 * @author Joe Walnes
 */
public class ContentBlockExtractingRule extends BasicBlockRule<String> {

    private final Content content;

    public ContentBlockExtractingRule(Content content) {
        this.content = content;
    }

    @Override
    protected String processStart(Tag tag) throws IOException {
        tagProcessorContext.pushBuffer();
        return tag.getAttributeValue("tag", false);
    }

    @Override
    protected void processEnd(Tag tag, String tagId) throws IOException {
        content.getProperty("page." + tagId).setValue(tagProcessorContext.currentBufferContents());
        tagProcessorContext.popBuffer();
    }

}