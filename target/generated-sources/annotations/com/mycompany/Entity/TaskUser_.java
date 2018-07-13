package com.mycompany.Entity;

import com.mycompany.Entity.Task;
import com.mycompany.Entity.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-07-13T11:42:18")
@StaticMetamodel(TaskUser.class)
public class TaskUser_ { 

    public static volatile SingularAttribute<TaskUser, Task> task;
    public static volatile ListAttribute<TaskUser, User> userList;
    public static volatile SingularAttribute<TaskUser, Long> id;
    public static volatile SingularAttribute<TaskUser, User> createdUser;
    public static volatile SingularAttribute<TaskUser, String> remarks;

}