package pl.com.uek.hd.webscraper;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

public interface Transformator<T> {
    T transform(HtmlElement htmlElement);
}
