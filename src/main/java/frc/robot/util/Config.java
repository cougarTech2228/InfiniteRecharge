package frc.robot.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configs must be Doubles, Integers, Strings, or Booleans, and to use them,
 * the parent class must extend ConfigurableSubsystem
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    /**
     * save to Config File
     */
    public boolean save() default true;
    /**
     * push to the Network Tables,
     */
    public boolean push() default true;
    /**
     * load from Config File
     */
    public boolean load() default true;
    /**
     * can value be changed on shuffleboard
     */
    public boolean readonly() default false;
}