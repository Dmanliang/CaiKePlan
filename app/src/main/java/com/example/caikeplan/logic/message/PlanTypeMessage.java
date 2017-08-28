package com.example.caikeplan.logic.message;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/6/1.
 */

public class PlanTypeMessage implements IPickerViewData {

    private String name;
    private List<BitBean>   bitBeen;

    public PlanTypeMessage(){
        bitBeen = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BitBean> getBitBeen() {
        return bitBeen;
    }

    public void setBitBeen(List<BitBean> bitBeen) {
        this.bitBeen = bitBeen;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public static class BitBean{
        private String name;
        private List<String> issues_list;

        public BitBean(){
            issues_list = new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getIssues_list() {
            return issues_list;
        }

        public void setIssues_list(List<String> issues_list) {
            this.issues_list = issues_list;
        }

        public void newList(){
            issues_list = new ArrayList<>();
        }
    }

}
