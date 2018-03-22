package com.mycompany.Entity;

import com.mycompany.Entity.Category;
import com.mycompany.Entity.User;
import com.mycompany.Enum.Priority;
import com.mycompany.Enum.TaskStatus;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-03-22T16:06:06")
@StaticMetamodel(Task.class)
public class Task_ { 

    public static volatile ListAttribute<Task, User> shareTaskList;
    public static volatile SingularAttribute<Task, Date> dueDate;
    public static volatile SingularAttribute<Task, String> name;
    public static volatile SingularAttribute<Task, Integer> completePercent;
    public static volatile SingularAttribute<Task, Long> id;
    public static volatile SingularAttribute<Task, Category> category;
    public static volatile SingularAttribute<Task, Priority> priority;
    public static volatile SingularAttribute<Task, TaskStatus> taskStatus;
    public static volatile SingularAttribute<Task, Date> startDate;
    public static volatile SingularAttribute<Task, String> remarks;

}