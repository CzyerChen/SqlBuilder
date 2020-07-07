package com.owl.sqlbuilder.entity.dto;

/**
 *
 * OLap正确返回数据封装列对象
 *
 * @author claire
 * @date 2019-11-07 - 10:49
 **/
public class ResponseColumnMeta {
    private int isNullable;
    private int displaySize;
    private String label;
    private String name;
    private String schemaName;
    private String catelogName;
    private String tableName;
    private int precision;
    private int scale;
    private int columnType;
    private String columnTypeName;
    private boolean signed;
    private boolean writable;
    private boolean autoIncrement;
    private boolean searchable;
    private boolean currency;
    private boolean definitelyWritable;
    private boolean caseSensitive;
    private boolean readOnly;

    public int getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(int isNullable) {
        this.isNullable = isNullable;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getCatelogName() {
        return catelogName;
    }

    public void setCatelogName(String catelogName) {
        this.catelogName = catelogName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getColumnType() {
        return columnType;
    }

    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isCurrency() {
        return currency;
    }

    public void setCurrency(boolean currency) {
        this.currency = currency;
    }

    public boolean isDefinitelyWritable() {
        return definitelyWritable;
    }

    public void setDefinitelyWritable(boolean definitelyWritable) {
        this.definitelyWritable = definitelyWritable;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
