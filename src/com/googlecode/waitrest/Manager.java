package com.googlecode.waitrest;

import com.googlecode.funclate.Model;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.annotations.HttpMethod;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.yadic.Container;

import java.util.Map;

import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.utterlyidle.Requests.*;
import static com.googlecode.utterlyidle.Responses.*;
import static com.googlecode.utterlyidle.Status.BAD_REQUEST;
import static com.googlecode.utterlyidle.Status.CREATED;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;
import static com.googlecode.waitrest.Predicates.*;
import static com.googlecode.waitrest.Renderers.fileRenderer;
import static com.googlecode.waitrest.Renderers.textRenderer;
import static com.googlecode.waitrest.Waitress.WAITRESS_ORDERS_PATH;
import static com.googlecode.waitrest.Waitress.WAITRESS_ORDER_PATH;

public class Manager implements ResourcesModule, ApplicationScopedModule, ResponseHandlersModule {
    @Override
    public Module addPerApplicationObjects(Container container) {
        container.add(Kitchen.class);
        return this;
    }

    @Override
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(Waitress.class));
        return this;
    }


    @Override
    public Module addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(method(HttpMethod.GET).and(path(WAITRESS_ORDER_PATH)), renderer(fileRenderer("menu.html")));
        handlers.add(method(HttpMethod.GET).and(path(WAITRESS_ORDERS_PATH)), renderer(textRenderer()));
        handlers.add(method(HttpMethod.POST).and(path(WAITRESS_ORDER_PATH)).and(status(BAD_REQUEST)), renderer(fileRenderer("menu.html")));
        handlers.add(method(HttpMethod.POST).and(path(WAITRESS_ORDER_PATH)).and(status(CREATED)).and(modelContainsHttpMethod(HttpMethod.GET)), renderer(fileRenderer("get.html")));
        handlers.add(method(HttpMethod.POST).and(path(WAITRESS_ORDER_PATH)).and(status(CREATED)), renderer(fileRenderer("notGet.html")));
        return this;
    }

    private Predicate<Pair<Request, Response>> modelContainsHttpMethod(final String httpMethod) {
        return new Predicate<Pair<Request, Response>>() {
            @Override
            public boolean matches(Pair<Request, Response> requestResponsePair) {
                return ((Model) requestResponsePair.second().entity()).get("method", String.class).equalsIgnoreCase(httpMethod);
            }
        };
    }

}
