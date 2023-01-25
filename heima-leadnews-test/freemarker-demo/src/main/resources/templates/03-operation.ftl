<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Hello World!</title>
</head>
<body>
<b>比较运算符</b>
<br/>
<dl>
    <dt>=/== 和 != 比较：</dt>
    <dd>
        <#if "xiaoming" == "xiaoming">
            字符串比较 "xiaoming" == "xiaoming"
        </#if>
    </dd>
    <dd>
        <#if 10 != 100>
            数值的比较 10 != 100
        </#if>
    </dd>
</dl>
<dl>
    <dt>其他比较</dt>
    <dd>
        <#if 10 gt 5>
            形式一：使用特殊字符比较数值 10 gt 5
        </#if>
    </dd>
    <dd>
        <#-- 日期的比较需要通过?date将属性转为data类型才能进行比较 -->
        <#if (date1?date >= date2?date)>
            形式二：使用括号形式比较时间 date1?date >= date2?date
        </#if>
    </dd>
</dl>
<br/>
<b>逻辑运算符</b>
<br/>
<br/>
<#if (10 lt 12 )&&( 10  gt  5 )  >
    (10 lt 12 )&&( 10  gt  5 )  显示为 true
</#if>
<br/>
<br/>
<#if !false>
    false 取反为true
</#if>
<hr>
</body>
</html>