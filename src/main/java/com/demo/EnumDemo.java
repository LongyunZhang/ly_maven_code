package com.demo;

public class EnumDemo {
    //银行代码
    public enum PinganBankCode {
        ICBC("36", "10001"),//工商银行
        ABC("37", "10002"),//农业银行
        BOC("39", "10003"),//中国银行
        COB("35", "10004"),//建设银行
        PSBC("41", "10005"),//邮政储蓄银行
        CMB("38", "10006"),//招商银行
        CITIC("44", "10007"),//中信银行
        CIB("85", "10008"),//兴业银行
        CEB("42", "10009"),//光大银行
        CMBC("45", "10010"),//民生银行
        CGB("47", "10011"),//广发银行
        SHANGHAI("111", "10012"),//上海银行
        BEIJING("76", "10013"),//北京银行
        NBCB("107", "10020");//宁波银行

        private String bankTypeId;
        private String bankCode;
        PinganBankCode(String bankTypeId, String bankCode){
            this.bankTypeId = bankTypeId;
            this.bankCode = bankCode;
        }
        public String getBankTypeId() {
            return bankTypeId;
        }
        public String getBankCode() {
            return bankCode;
        }
    }
}
