package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/6/5.
 */

public class IssueTitleMessage {

    private String  issueTitle;
    private boolean isFocus=false;
    private int  tables;

    public IssueTitleMessage(String issueTitle, int tables, boolean isFocus){
        this.issueTitle =   issueTitle;
        this.tables     =   tables;
        this.isFocus    =   isFocus;

    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public int getTables() {
        return tables;
    }

    public void setTables(int tables) {
        this.tables = tables;
    }
}
