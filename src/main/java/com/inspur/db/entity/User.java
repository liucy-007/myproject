package com.inspur.db.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Lombok依赖
 * @author WX0liucy
 * 生成一个该类的构造方法，禁止无参构造@RequiredArgsConstructor
 * 重写该类的toString()方法@ToString
 * 重写该类的equals()和hashCode()方法@EqualsAndHashCode
 * 注解@Data等价于上面的@Setter、@Getter、@RequiredArgsConstructor、@ToString、@EqualsAndHashCode lombok的@Builder实际是建造者模式的一个变种，所以在创建复杂对象时常使用
 */

/**
 * 表明该类 (User) 为一个实体类，它默认对应数据库中的表名是user
 */

@Data
@Builder//用一个内部类去实例化一个对象，避免一个类出现过多构造函数，可以直接通过Builder设置字段参数
@AllArgsConstructor//添加一个构造函数，该构造函数含有所有已声明字段属性参数
@NoArgsConstructor //使用后创建一个无参构造函数
/**
 * 一个对象序列化的接口，一个类只有实现了Serializable接口，它的对象才是可序列化的
 * 内存中的对象写入到硬盘,在进行Java的Socket编程的时候，你有时候可能要传输某一类的对象
 * 如果要通过远程的方法调用（RMI）去调用一个远程对象的方法
 */
public class User implements Serializable {
    /**
     * 声明此属性为主键。该属性值可以通过应该自身创建
     */
    @Id
    /**
     *  声明该属性与数据库字段的映射关系。
     */

    private String id;

    private String username;

    private String password;

    @JSONField(name = "createdTime", format = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createTime;

}



























