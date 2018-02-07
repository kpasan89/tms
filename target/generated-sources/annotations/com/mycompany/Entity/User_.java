package com.mycompany.Entity;

import com.mycompany.Entity.Location;
import com.mycompany.Enum.AccountType;
import com.mycompany.Enum.Rank;
import com.mycompany.Enum.UserStatus;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-02-07T16:24:12")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, AccountType> accountType;
    public static volatile SingularAttribute<User, Date> joinedDate;
    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, Rank> rank;
    public static volatile SingularAttribute<User, Location> location;
    public static volatile SingularAttribute<User, String> cunfirm_password;
    public static volatile SingularAttribute<User, Boolean> retired;
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> remarks;
    public static volatile SingularAttribute<User, String> username;
    public static volatile SingularAttribute<User, UserStatus> status;

}