package com.ling.blutoothlibrary.bleenum;

/**
 * 创建时间 ：2017/12/8.
 * <br/>作者 ：红叶岭谷
 * <br/>说明 ：这是显示对话框的枚举
 * <br/>备注 ：不可以混淆
 */
public enum DialogEnum {
        /**
         * 确定
         */
        DIALOG_POSITIVE("确定",0),
        /**
         * 取消
         */
        DIALOG_NEGATIVE("取消",1);

        private String name;
        private int idex;
        DialogEnum(String enumName, int enumIdex){
            this.name = enumName;
            this.idex = enumIdex;
        }
}
