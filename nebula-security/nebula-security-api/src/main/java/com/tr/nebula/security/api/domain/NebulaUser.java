package com.tr.nebula.security.api.domain;

/**
 * Created by Mustafa Erbin on 21.03.2017.
 */
public interface NebulaUser {
    /**
     * @return
     */
    Object getUserId();

    /**
     * @return
     */
    String getUsername();


    /**
     * @return
     */
    String getPassword();

    /**
     * @return
     */
    boolean isActive();

    /**
     * @return
     */
    NebulaRole getRole();

    /**
     *
     * @return
     */
    String getName();

    /**
     *
     * @return
     */
    String getSurname();
}
