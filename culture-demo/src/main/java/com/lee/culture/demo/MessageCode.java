package com.lee.culture.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by zhengjun.jing on 7/13/2017.
 */
public enum MessageCode {
    /** ===========系统公用 Message Code===================*/
    /** 前缀 0000*/
    COMMON_SUCCESS("0000_0","执行成功"),
    COMMON_FAILURE("0000_1", "执行失败"),
    COMMON_NO_AUTHORIZED("0000_2","没有权限执行"),
    COMMON_NO_DATA("0000_3","查询不到对应数据"),
    COMMON_PARAMETER_ERROR("0000_4","参数错误"),
    COMMON_UNKNOWN_ERROR("0000_11","未知异常"),
    COMMON_DB_ERROR("0000_12","数据库操作异常"),
    COMMON_API_ERROR("0000_13","操作异常"),
    COMMON_SERVICE_ERROR("0000_14","服务异常"),
    COMMON_USER_NOT_EXIST("0000_15","用户不存在"),
    COMMON_DATA_NOT_EXIST("0000_16","数据不存在"),
    COMMON_UPLOAD_FILE_EXTENSION_ERROR("0000_17","上传文件格式不正确！"),
    COMMON_USERNAME_IS_USED("0000_18","用户名已被使用"),
    COMMON_PHONE_FORMAT_ERROR("0000_19","该手机号码不符合规则"),
    COMMON_PHONE_IS_USED("0000_20","该联系电话已被使用"),
    COMMON_EMAIL_IS_USED("0000_21","该邮箱已被使用"),
    COMMON_IMPORT_FAILURE("0000_22","文件导入存在失败数据"),

    /** ========= 用户管理 Message Code====================*/
    /** 前缀 0001*/
    USER_INVALIDATE_PHONE("0001_1","手机号码不存在"),
    OLD_PASSWORD_ERROR("0001_2", "旧密码不正确"),
    USER_PHONE_EXIST("0001_3", "该联系电话已被使用"),
    USER_EMAIL_EXIST("0001_4", "该邮箱已被使用"),
    USER_PHONE_NOT_MATCH("0001_5", "手机号码和用户名不匹配"),
    USER_ROLE_NAME_EXIST("0001_6", "角色名称已存在"),



    

    /** ========= 订单管理 Message Code =================== */
    /**
     * 前缀 0002
     */
    ORDER_MONTH_LIMIT_LEFT_OVERDRAFT("0002_1", "月度余额不足"),
    ORDER_RULE_UNSATISFACTORY("0002_2", "用车规则不满足"),
    ORDER_DATE_FORMAT_ERROR("0002_3", "日期格式错误"),





    /**============车辆管理 Message Code=====================*/
    /** 前缀 0003*/
    
    VEHICLE_NUMBER_EXIST("0003_1","该车牌号已存在，请重新输入！"),
    VEHICLE_IDENTIFICATION_EXIST("0003_2","该车牌号已被绑定，请重新输入！"),
    VEHICLE_UPDATE_SPEED_LIMIT_FAIL("0003_3","下发限速失败！"),
    VEHICLE_UPDATE_SPEED_LIMIT_FAIL_WITHOUT_DEVICE("0003_4","车辆未绑定设备,下发限速失败！"),
    




	/**============部门管理 Message Code=====================*/
    /** 前缀 0004*/
	
	DEPT_HAS_RESOURCE_UPDATE("0004_1","该部门有资源信息，无法修改上级部门！"),
	DEPT_HAS_RESOURCE_DELETE("0004_2","该部门有资源信息，删除失败！"),

	
	/**============设备管理Message Code=====================*/
    /** 前缀 0005*/
	SNNUMBER_EXIST("0005_1","snNumber已经存在！"),
	IMEINUMBER_EXIST("0005_2","imeiNumber已经存在！"),
	ICCIDNUMBER_EXIST("0005_3","iccidNumber已经存在！"),
	SIMNUMBER_EXIST("0005_4","SIM卡已经存在！"),
	VEHICLE_NUMBER_ALREADY_BIND("0005_5","该车牌号已经绑定！"),
	SIM_NUMBER_ALREADY_BIND("0005_6","该SIM卡号已经绑定！"),
	DEVICE_BIND_LICENSE_FAILTRUE("0005_7"),
	LICENSE_ACTIVATE_FAILTRUE("0005_8"),
	LICENSE_SUSPEND_FAILTRUE("0005_9"),
	LICENSE_REACTIVATE_FAILTRUE("0005_10"),
	LICENSE_TERMINATED_FAILTRUE("0005_11"),
	LICENSE_UNBIND_FAILTRUE("0005_12"),
	
	/**============司机管理Message Code=====================*/
    /** 前缀 0006*/
	DRIVER_LICENSEBEGINTIME_FORMAT_ERROR("0006_1","初次领证时间格式错误！"),
	DRIVER_LICENSEEXPIRETIME_FORMAT_ERROR("0006_2","驾照到期时间格式错误！"),
	DRIVER_BIRTHDAY_FORMAT_ERROR("0006_3","出生日期格式错误！"),
	DRIVER_IMPORT_LICENSETYPE_ISBLANK("0006_4","驾照类型为空,导入失败!"),
	DRIVER_IMPORT_LICENSENUMBER_ISBLANK("0006_5","驾照号码为空,导入失败!"),
	DRIVER_IMPORT_LICENSENUMBER_EXIST("0006_6","驾照号码已存在,导入失败!"),
	DRIVER_IMPORT_LICENSENUMBER_FORMAT_ERROR("0006_7","驾照号码格式错误,导入失败!"),
	DRIVER_IMPORT_LICENSEBEGINTIME_ISBLANK("0006_8","初次领证时间为空,导入失败!"),
	DRIVER_IMPORT_LICENSEBEGINTIME_GREATER_NOW("0006_9","初次领证时间大于现在的时间,导入失败!"),
	DRIVER_IMPORT_BIRTHDAY_ISBLANK("0006_10","出生日期为空,导入失败!"),
	DRIVER_IMPORT_ISMINOR_ERROR("0006_11","出生日期未满18周岁,导入失败!"),
	DRIVER_IMPORT_LICENSEBEGINTIME_ISMINOR("0006_12","初次领证时间未满18周岁,导入失败!"),
	DRIVER_IMPORT_LICENSEEXPIRETIME_ISBLANK("0006_13","驾照到期时间为空,导入失败!"),
	DRIVER_IMPORT_LICENSEBEGINTIME_GREATER_EXPIRETIME("0006_14","驾照到期时间应大于初次领证时间,导入失败!"),
	DRIVER_IMPORT_STATION_ENT_DRIVER_DIFF("0006_15","站点所属企业与司机所属企业不一致,导入失败!"),
	DRIVER_IMPORT_STATION_NAME_NOT_EXIST("0006_16","所属企业找不到站点名称,导入失败!"),
	DRIVER_NOT_EXIST("0006_17","司机不存在"),
	DRIVER_LICENSE_NUMBER_IS_USED("0006_18","该驾照号码已被使用"),
	
	/**============文件导入导出管理Message Code=====================*/
    /** 前缀 0007*/
	FILE_IMPORT_USERNAME_ISBLANK("0007_1","登录名为空,导入失败!"),
	FILE_IMPORT_REALNAME_ISBLANK("0007_2","真实姓名为空,导入失败!"),
	FILE_IMPORT_PHONE_ISBLANK("0007_3","联系电话为空,导入失败!"),
	FILE_IMPORT_PHONE_FORMAT_ERROR("0007_4","联系电话不合法,导入失败!"),
	FILE_IMPORT_ORGANIZATIONNAME_ISBLANK("0007_5","所属企业为空,导入失败!"),
	FILE_IMPORT_SEX_ISBLANK("0007_6","性别为空,导入失败!"),
	FILE_IMPORT_USERNAME_EXIST("0007_7","登陆名已被使用,导入失败!"),
	FILE_IMPORT_PHONE_EXIST("0007_8","联系电话已被使用,导入失败!"),
	FILE_IMPORT_EMAIL_EXIST("0007_9","邮箱已被使用,导入失败!"),
	FILE_IMPORT_EMAIL_FORMAT_ERROR("0007_10","邮箱格式错误,导入失败!"),
	FILE_IMPORT_PASSWORD_FORMAT_ERROR("0007_11","密码不符合要求，密码由6-20位数字、字母组成,导入失败!"),
	FILE_IMPORT_ENT_DEFF_ERROR("0007_12","企业名称与当前企业不一致,导入失败!"),
	FILE_IMPORT_ENTNAME_ISBLANK("0007_13","请填写所属企业名称,导入失败!"),
	FILE_IMPORT_ENTNAME_NOT_EXIST("0007_14","找不到企业名称,导入失败!"),
	FILE_IMPORT_DEP_DEFF_ERROR("0007_15","部门所属企业与当前企业不一致,导入失败!"),
	FILE_IMPORT_DEPNAME_ISBLANK("0007_16","请填写正确的部门名称,导入失败!"),
	FILE_IMPORT_DEPNAME_NOT_EXIST("0007_17","找不到部门名称,导入失败!"),
	
	

	
	
	/**============员工管理Message Code=====================*/
    /** 前缀 0008*/
	EMP_NOT_EXIST("0008_1","员工不存在"),
	
	
	/**============公告消息Message Code=====================*/
	MESSAGE_CENTER_EXIST("0009_1","消息不存在"),
	MESSAGE_CENTER_READ("0009_2","消息已经置为已读"),
	
	/**============车辆维保Message Code=====================*/
	HEAD_MAINTENANCE_MILEAGE_LESSTHAN_LASTTIME("0010_1","本次保养表头里程数小于上次保养表头里程数,未更新!"),
	MAINTENANCE_TIME_BEFORE_CURRENT_MAINTENANCE_TIME("0010_2","本次保养时间小于当前记录的保养时间,未更新!"),
	
	IAM_PATCH_GROUP_ERRO("0011_2","设置用户与组失败"),
	IAM_CREATE_USER_ERRO("0011_1","在IAM中创建用户失败");
    //Message 编码
    private String code;
    //Message 描叙
    private String message;

    MessageCode(String code){
        this.code = code;
    }
    
    MessageCode(String code,String message){
        this.code = code;
        this.message = message;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }


    @JsonCreator
    public static MessageCode getStatusCode(String status) {
        for (MessageCode unit : MessageCode.values()) {
            if (unit.getCode().equals(status)) {
                return unit;
            }
        }

        return null;
    }


    @Override
    public String toString() {
        return "{code:'" + code + '\'' +
                ", message:'" + message + '\'' +
                '}';
    }
}
