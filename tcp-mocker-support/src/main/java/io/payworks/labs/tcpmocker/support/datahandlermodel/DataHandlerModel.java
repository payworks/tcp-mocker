package io.payworks.labs.tcpmocker.support.datahandlermodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import io.payworks.labs.tcpmocker.datahandler.DataHandlerType;

import java.util.List;
import java.util.Objects;

public class DataHandlerModel {

    public static final class Request {
        private List<String> matchesList;

        @JsonCreator
        public Request(@JsonProperty("matches") final List<String> matchesList) {
            this.matchesList = matchesList;
        }

        @JsonProperty("matches")
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        public List<String> getMatchesList() {
            return ImmutableList.copyOf(matchesList);
        }

        @JsonIgnore
        public String getMatches() {
            return String.join("", matchesList);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Request request = (Request) o;
            return Objects.equals(matchesList, request.matchesList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(matchesList);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("matchesList", matchesList)
                    .toString();
        }
    }

    public static final class Response {
        private List<String> dataList;

        @JsonCreator
        public Response(@JsonProperty("data") final List<String> dataList) {
            this.dataList = dataList;
        }

        @JsonProperty("data")
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        public List<String> getDataList() {
            return ImmutableList.copyOf(dataList);
        }

        @JsonIgnore
        public String getData() {
            return String.join("", dataList);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Response response = (Response) o;
            return Objects.equals(dataList, response.dataList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(dataList);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("dataList", dataList)
                    .toString();
        }
    }

    private DataHandlerType handlerType;
    private Integer order;
    private List<Request> requestList;
    private Response response;

    @JsonCreator
    public DataHandlerModel(@JsonProperty("handlerType") DataHandlerType handlerType,
                            @JsonProperty("order") Integer order,
                            @JsonProperty("request") List<Request> requestList,
                            @JsonProperty("response") Response response) {
        this.handlerType = handlerType;
        this.order = order;
        this.requestList = requestList;
        this.response = response;
    }

    @JsonProperty("handlerType")
    public DataHandlerType getHandlerType() {
        return handlerType;
    }

    @JsonProperty("order")
    public Integer getOrder() {
        return order;
    }

    @JsonProperty("request")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<Request> getRequestList() {
        return ImmutableList.copyOf(requestList);
    }

    @JsonIgnore
    public Request getRequest() {
        return Iterables.getFirst(requestList, null);
    }

    @JsonProperty("response")
    public Response getResponse() {
        return response;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataHandlerModel that = (DataHandlerModel) o;
        return handlerType == that.handlerType &&
                Objects.equals(order, that.order) &&
                Objects.equals(requestList, that.requestList) &&
                Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handlerType, order, requestList, response);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("handlerType", handlerType)
                .add("order", order)
                .add("requestList", requestList)
                .add("response", response)
                .toString();
    }
}
