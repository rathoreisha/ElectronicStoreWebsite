package com.shruteekatech.ecommerce.constant;

public class ValidationConstant {

    public static  final String USER="Username must be in 4 characters !!";
    public static final String PASSWORD="Minimum eight characters, at least one letter, one number and one special character:";
    public static final  String EMAIL="Email should be in correct format !! ex: abc@gmail.com";

    public static final String ABOUT = "Please Enter  Atleast more than 10 characters";
    public static final String GENDER_MSG ="Please enter correct Gender" ;
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    public static final String TITLE_MSG = "Please Enter More than 5 Charcaters Title";
    public static final String DESCRIPTION_MSG = "Please Enter More than 5 Charcaters Description";
    public static final String CATEGORY = "Title should not be null or more than 5 characters";

    public static final String PRODUCT ="Title should not be null or more than 5 characters";
    public static final String PAGE_SIZE ="5";

    public static final  String PAGE_NUMBER="0";

    public  static final String SORT_BY_CAT="categoryId";
    public  static final String SORT_BY_USER="id";
    public  static final String SORT_BY_PRODUCT="pid";
    public static final String SORT_DIR="asc";



}
