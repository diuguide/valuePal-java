package com.example.valuepaljava.models;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name="market")
public class SummaryObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @Column(name="full_name")
    private String fullExchangeName;

    @Column(name="exchange")
    private String exchange;

    @ElementCollection
    @Column(name="timestamp")
    private List<Integer> timestamp;

    @ElementCollection
    @Column(name="close")
    private List<Double> close;

    public SummaryObject() {
    }

    public SummaryObject(Integer recordId, String fullExchangeName, String exchange, List<Integer> timestamp, List<Double> close) {
        this.recordId = recordId;
        this.fullExchangeName = fullExchangeName;
        this.exchange = exchange;
        this.timestamp = timestamp;
        this.close = close;
    }

    @Override
    public String toString() {
        return "SummaryObject{" +
                "fullExchangeName='" + fullExchangeName + '\'' +
                ", exchange='" + exchange + '\'' +
                ", timestamp=" + timestamp +
                ", close=" + close +
                '}';
    }

    public String getFullExchangeName() {
        return fullExchangeName;
    }

    public void setFullExchangeName(String fullExchangeName) {
        this.fullExchangeName = fullExchangeName;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public List<Integer> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(List<Integer> timestamp) {
        this.timestamp = timestamp;
    }

    public List<Double> getClose() {
        return close;
    }

    public void setClose(List<Double> close) {
        this.close = close;
    }
}
