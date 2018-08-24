# leoyork-permission
user-permission
权限控制结果：
三种：1.不允许调用  2.返回部分结果  3.List中屏蔽部分

权限属性构成：
两种：1.数字level（升序降序都有可能）  2.字符串形式（字符串相同则拥有权限）

使用方法，不与其他注解等冲突：
给要使用的方法加@Permission注解，方法需要参数Power类型。对于部分返回的返回值类型对不想要返回的值加@RefuseField。对于List返回时部分屏蔽对需要校验权限的部分加@BlockField。

Power属性：resource（字符串形式） level（数字形式）  两种择一使用，通过判空resource与下述注解对应，优先level形式

@Permission属性：value（需求的level） 
sort（权限排序方式，ASC表示level越大权限越高，DESC相反） 值为LevelSort枚举类
resource（字符串形式的权限，多个权限可用逗号隔开）
type（控制形式，REFUSE为拒绝访问，RETURN_PART为部分返回，LIST_PART为列表部分返回同时包含RETURN_PART部分）值为PermissionType枚举类
