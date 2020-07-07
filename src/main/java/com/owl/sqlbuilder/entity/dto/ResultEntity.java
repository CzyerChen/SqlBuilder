package com.owl.sqlbuilder.entity.dto;

import java.util.List;

/**
 * OLap正确返回数据封装列对象
 *
 * @author claire
 * @date 2019-11-07 - 11:32
 **/
public class ResultEntity {

    private List<ResponseColumnMeta> columnMetas;
    private List<List<String>>  results;
    private String cube;
    private int affectedRowCount;
    private String isException;
    private String exceptionMessage;
    private int duration;
    private int totalScanCount;
    private int totalScanBytes;
    private String hitExceptionCache;
    private String storageCacheUsed;
    private String traceUrl;
    private String pushDown;
    private String partial;


    public List<ResponseColumnMeta> getColumnMetas() {
        return columnMetas;
    }

    public void setColumnMetas(List<ResponseColumnMeta> columnMetas) {
        this.columnMetas = columnMetas;
    }

    public List<List<String>> getResults() {
        return results;
    }

    public void setResults(List<List<String>> results) {
        this.results = results;
    }

    public String getCube() {
        return cube;
    }

    public void setCube(String cube) {
        this.cube = cube;
    }

    public int getAffectedRowCount() {
        return affectedRowCount;
    }

    public void setAffectedRowCount(int affectedRowCount) {
        this.affectedRowCount = affectedRowCount;
    }

    public String getIsException() {
        return isException;
    }

    public void setIsException(String isException) {
        this.isException = isException;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalScanCount() {
        return totalScanCount;
    }

    public void setTotalScanCount(int totalScanCount) {
        this.totalScanCount = totalScanCount;
    }

    public int getTotalScanBytes() {
        return totalScanBytes;
    }

    public void setTotalScanBytes(int totalScanBytes) {
        this.totalScanBytes = totalScanBytes;
    }

    public String getHitExceptionCache() {
        return hitExceptionCache;
    }

    public void setHitExceptionCache(String hitExceptionCache) {
        this.hitExceptionCache = hitExceptionCache;
    }

    public String getStorageCacheUsed() {
        return storageCacheUsed;
    }

    public void setStorageCacheUsed(String storageCacheUsed) {
        this.storageCacheUsed = storageCacheUsed;
    }

    public String getTraceUrl() {
        return traceUrl;
    }

    public void setTraceUrl(String traceUrl) {
        this.traceUrl = traceUrl;
    }

    public String getPushDown() {
        return pushDown;
    }

    public void setPushDown(String pushDown) {
        this.pushDown = pushDown;
    }

    public String getPartial() {
        return partial;
    }

    public void setPartial(String partial) {
        this.partial = partial;
    }
}
