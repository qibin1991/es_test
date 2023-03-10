package com.valid;

import javax.validation.groups.Default;

/**
 * @ClassName CustomValidateGroup
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/23 10:47
 * @Version 1.0
 * @mark: show me the code , change the world
 */

public interface CustomValidateGroup extends Default {

    interface Crud extends CustomValidateGroup {
        interface Create extends Crud {

        }

        interface Update extends Crud {

        }

        interface Query extends Crud {

        }

        interface Delete extends Crud {

        }
    }
}
