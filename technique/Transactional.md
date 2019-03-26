# Spring 事务详解

## @Transactional 注解

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {
    
    //通过配置xml或配置@Bean声明transactionManager,事务才会起作用
    @AliasFor("transactionManager")
    String value() default "";
	
    @AliasFor("value")
    String transactionManager() default "";

    //该属性用于设置事务的传播行为，具体取值可参考表6-7。
    //例如：@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
    Propagation propagation() default Propagation.REQUIRED;
    
	//该属性用于设置底层数据库的事务隔离级别，事务隔离级别用于处理多事务并发的情况，
    //通常使用数据库的默认隔离级别即可，基本不需要进行设置
    Isolation isolation() default Isolation.DEFAULT;

    //该属性用于设置事务的超时秒数，默认值为-1表示永不超时
    int timeout() default -1;
    
	//该属性用于设置当前事务是否为只读事务，设置为true表示只读，false则表示可读写，默认值为false。
    //例如：@Transactional(readOnly=true)
    boolean readOnly() default false;

    //该属性用于设置需要进行回滚的异常类数组，当方法中抛出指定异常数组中的异常时，则进行事务回滚。
    //例如：指定单一异常类：@Transactional(rollbackFor=RuntimeException.class)
	//指定多个异常类：@Transactional(rollbackFor={RuntimeException.class, Exception.class})
    Class<? extends Throwable>[] rollbackFor() default {};

    //该属性用于设置需要进行回滚的异常类名称数组，当方法中抛出指定异常名称数组中的异常时，则进行事务回滚。
    //例如：
    //指定单一异常类名称：@Transactional(rollbackForClassName="RuntimeException")
	//指定多个异常类名称：@Transactional(rollbackForClassName={"RuntimeException","Exception"})
    String[] rollbackForClassName() default {};

    //该属性用于设置不需要进行回滚的异常类数组，当方法中抛出指定异常数组中的异常时，不进行事务回滚。
    //例如：
    //指定单一异常类：@Transactional(noRollbackFor=RuntimeException.class)
    //指定多个异常类：@Transactional(noRollbackFor={RuntimeException.class, Exception.class})
    Class<? extends Throwable>[] noRollbackFor() default {};

    //该属性用于设置不需要进行回滚的异常类名称数组，当方法中抛出指定异常名称数组中的异常时，不进行事务回滚。
    //例如：
    //指定单一异常类名称：@Transactional(noRollbackForClassName="RuntimeException")
    //指定多个异常类名称：@Transactional(noRollbackForClassName={"RuntimeException","Exception"})
    String[] noRollbackForClassName() default {};
}
```

## 事务传播属性

```
@Transactional( propagation = Propagation.REQUIRED )
```

1. Propagation.REQUIRED

   如果有事务, 那么加入事务, 没有的话新建一个事务，Spring 默认属性

2. Propagation.SUPPORTS

   如果其他bean调用这个方法,在其他bean中声明事务,那就用事务.如果其他bean没有声明事务,那就不用事务.

3. Propagation.MANDATORY

   必须在一个已有的事务中执行,否则抛出异常

4. Propagation.REQUIRES_NEW

   不管是否存在事务,都创建一个新的事务,原来的挂起,新的执行完毕,继续执行老的事务

5. Propagation.NOT_SUPPORTED

   这个方法不开启事务

6. Propagation.NEVER

   必须在一个没有的事务中执行,否则抛出异常

7. Propagation.NESTED

   如果一个活动的事务存在，则运行在一个嵌套的事务中，如果没有活动的事务，则按照REQUIRED属性执行，它使用一个单独的事务。这个事务拥有多个回滚的保存点，内部事务的回滚不会对外部事务造成影响，它只对DataSource TransactionManager事务管理器起效。

## 事务隔离级别

```
@Transactional( isolation = Isolation.DEFAULT)
```

1. Isolation.DEFAULT 	默认：使用数据库的事务隔离级别

   MYSQL：默认为REPEATABLE_READ级别

   SQLSERVER：默认为READ_COMMITTED级别

   ORACLE：默认READ_COMMITTED级别

2. Isolation.READ_UNCOMMITTED

   读取未提交数据(会出现脏读, 不可重复读) ，基本不使用

3. Isolation.READ_COMMITTED

   读取已提交数据(会出现不可重复读和幻读)

4. Isolation.REPEATABLE_READ

   可重复读(会出现幻读)

5. Isolation.SERIALIZABLE

   串行化

| 隔离级别                 |  脏读  | 不可重复读 |  幻读  |
| :----------------------- | :----: | :--------: | :----: |
| 读未提交READ_UNCOMMITTED |  可能  |    可能    |  可能  |
| 读已提交READ_COMMITTED   | 不可能 |    可能    |  可能  |
| 可重复读REPEATABLE_READ  | 不可能 |   不可能   |  可能  |
| 可串行化SERIALIZABLE     | 不可能 |   不可能   | 不可能 |

**脏读 :** 一个事务读取到另一事务未提交的更新数据

**不可重复读 : **在同一事务中, 多次读取同一数据返回的结果有所不同, 换句话说，后续读取可以读到另一事务已提交的更新数据. 相反,"可重复读"。

**可重复读：**在同一事务中多次读取数据时, 能够保证所读数据一样, 也就是后续读取不能读到另一事务已提交的更新数据。

**幻读 : **一个事务读到另一个事务已提交的insert数据。

## 注意事项

- @Transactional 只能被应用到public方法上，否则事务无效。