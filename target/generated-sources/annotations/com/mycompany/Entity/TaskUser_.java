package com.mycompany.Entity;

import com.mycompany.Entity.Task;
import com.mycompany.Entity.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-07T16:24:12")
@StaticMetamodel(TaskUser.class)
public class TaskUser_ { 

    public static volatile SingularAttribute<TaskUser, Task> task;
    public static volatile SingularAttribute<TaskUser, Long> id;
    public static volatile SingularAttribute<TaskUser, String> remarks;
    public static volatile SingularAttribute<TaskUser, User> username;

}