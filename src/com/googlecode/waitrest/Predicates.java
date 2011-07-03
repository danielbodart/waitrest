package com.googlecode.waitrest;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.utterlyidle.QueryParameters;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.Status;

import static com.googlecode.totallylazy.Callables.first;
import static com.googlecode.totallylazy.Callables.second;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Predicates {
    public static LogicalPredicate<Pair<Request, Response>> path(final String path) {
        return where(first(Request.class), where(Callables.stringPath(), is(path)));
    }

    public static LogicalPredicate<Pair<Request, Response>> status(final Status status) {
        return where(second(Response.class), where(Callables.status(), is(status)));
    }

    public static LogicalPredicate<Pair<Request, Response>> method(final String method) {
        return where(first(Request.class), where(Callables.method(), is(method)));
    }

    public static Predicate<QueryParameters> contains(final QueryParameters optionalParams) {
        return new Predicate<QueryParameters>() {
            @Override
            public boolean matches(QueryParameters requiredParams) {
                return sequence(requiredParams).forAll(in(sequence(optionalParams)));
            }
        };
    }
}
