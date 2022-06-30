package com.ibm.openpages.ext.interfaces.cmp.constant;


import com.ibm.openpages.ext.interfaces.common.constant.MessageConstants;

public class EntitlementMessageConstants extends MessageConstants {

    public static final String ERROR_FAILED_ADD_GROUP = "Failed to add user {0} to role {1}.";
    public static final String ERROR_FAILED_ADDING_PRODUCT_FUNCTION_TEAM = "Failed assigning Product Function Team {0} for user {1}.";
    public static final String ERROR_FAILED_COUNTRY_ADDED = "Failed to add user {0} to country {1}.";
    public static final String ERROR_FAILED_ADD_HELP_DESK = "Failed to add user {0} to Help desk {1}.";
    public static final String ERROR_FAILED_REMOVE_HELP_DESK = "Failed to remove user {0} from Help desk.";
    public static final String ERROR_FAILED_CREATING_USER = "Failed creating user {0}.";
    public static final String ERROR_FAILED_DELETE_NON_EXISTENT_USER = "Attempting to delete non-existent user {0}.";
    public static final String ERROR_FAILED_DELETE_USER = "Failed to delete user {0}.";
    public static final String ERROR_FAILED_MODIFY_NON_EXISTENT_USER = "Attempting to modify non-existent user {0}.";
    public static final String ERROR_FAILED_MODIFY_USER = "Failed modify user {0}.";
    public static final String ERROR_FAILED_REMOVE_GROUP = "Failed removing user {0} for group {1}.";
    public static final String ERROR_FAILED_REMOVE_GROUP_COUNTRY = "Failed removing country {0} for user {1}.";
    public static final String ERROR_FAILED_REMOVING_PRODUCT_FUNCTION_TEAM = "Failed removing Product Function Team {0} for user {1}.";

    public static final String ERROR_USER_ALREADY_EXIST = "Attempting to create user that already exists {0}.";
    public static final String USER_NOT_EXIST = "The user {0} not exists.";
    public static final String USER_EXIST = "The user {0} already exists.";
    public static final String ERROR_ADDING_PROFILE_USER = "Failed adding profile user.";
    public static final String ERROR_UPDATING_PROFILE_USER = "Failed updating profile user.";
    public static final String ERROR_FAILED_USER_NOT_BELONG_COUNTRY = "Failed removing country {0} for user {1}. The user is not member of that country.";
    public static final String ERROR_FAILED_REMOVE_ALL_GROUP = "Failed removing group {0} for user {1}.";
    public static final String ERROR_USER_IS_NOT_ALLOWED = "Failed to assign Lead Product Team. The user {0} is not member of {1}.";
    public static final String ERROR_USER_ID_DOES_NOT_BELONG = "Id of {0} does not belong to {1} Entity Level.";

    public static final String ERROR_INVALID_ROLE_TEMPLATE_AUDITOR_ROLE = "Failed to assign Product Function Team.";
    public static final String ERROR_USER_IS_NOT_MEMBER_OF_PRODUCT_FUNCTION_GROUP = "Failed to assign Product Function Team. The user {0} is not member of {1}.";

    public static final String ERROR_RESTORE_GROUPS_FAILED = "Failed restoring groups.";
    public static final String ERROR_RESTORE_ROLES_FAILED = "Failed restoring roles.";
    public static final String ERROR_RESTORE_USER_INFORMATION_FAILED = "Failed restoring user information.";


    public static final String WARNING_EMPTY_LIST = "The list is empty.";
    public static final String WARNING_USER_ALREADY_ADDED_GROUP = "The user {1} was already member of role {0}.";
    public static final String WARNING_USER_WAS_NOT_MEMBER_GROUP = "The user {0} is not a member of {1}.";
    public static final String WARNING_USER_ALREADY_ADDED_COUNTRY = "The user {1} was already member of country {0}.";
    public static final String WARNING_USER_ALREADY_ADDED_HELP_DESK = "The user {0} was already member of Help desk.";
    public static final String WARNING_USER_WAS_NOT_MEMBER_HELP_DESK = "The user {0} was not member of Help desk.";

    public static final String SUCCESS_ADDING_ROLE_ASSIGNMENT_LEAD_PRODUCT_TEAM = "Successfully assigned Lead Product Team {0} for user {1}.";
    public static final String SUCCESS_UPDATING_USER = "Successfully updated user {0}.";
    public static final String SUCCESS_CREATING_USER = "Successfully created user {0}.";
    public static final String SUCCESS_GROUPS_ADDED = "Successfully added role {0} to {1}.";
    public static final String SUCCESS_GROUPS_COUNTRY_ADDED = "Successfully added country {0} to {1}.";
    public static final String SUCCESS_ADD_HELP_DESK = "Successfully added Help desk {0} to {1}.";
    public static final String SUCCESS_GROUPS_COUNTRY_REMOVE = "Successfully removed country {0} for {1}.";
    public static final String SUCCESS_GROUPS_REMOVE = "Successfully removed role {0} for {1}.";
    public static final String SUCCESS_LEAD_PRODUCT_TEAM_REMOVE = "Successfully removed Lead Product Team {0} for {1}.";
    public static final String SUCCESS_DELETED_USER = "Successfully deleted user {0}.";
    public static final String SUCCESS_ADDING_PROFILE_USER = "Successfully adding profile user";
    public static final String SUCCESS_UPDATING_PROFILE_USER = "Successfully updating profile user";
    public static final String SUCCESS_REMOVE_HELP_DESK = "Successfully removed Help desk {0}";
    public static final String SUCCESS_RESTORE_IA_DIVISION = "Successfully restored Lead Product Team {0} for {1}.";
    public static final String SUCCESS_RESTORE_PRODUCT_FUNCTION_TEAM = "Successfully restored Product Function Team {0} for {1}.";
    public static final String SUCCESS_ADDING_PRODUCT_FUNCTION_TEAM = "Successfully assigned Product Function Team {0} for user {1}.";
    public static final String SUCCESS_REMOVING_PRODUCT_FUNCTION_TEAM = "Successfully removed Product Function Team {0} for user {1}.";

    public static final String SUCCESS_AUDITOR_ROLE_REMOVE = "Successfully removed Auditor role {0} for {1}.";
    public static final String SUCCESS_ASSIGNMENTS_REMOVE = "Successfully removed role assignment {0} for {1}.";
    public static final String SUCCESS_LOCK_USER = "Successfully locked user {0}.";
    public static final String ERROR_FAILED_REMOVING_ASSIGNMENT = "Failed removing role assignment {0} for user {1}.";
    public static final String ERROR_FAILED_ASSIGNMENT_REMOVE = "Failed removing invalid role assignment.";
    public static final String ERROR_FAILED_ADDING_ROLE_ASSIGNMENT_AUDITOR_ROLE = "Failed assigning Product Function Team {0} for user {1}.";
    public static final String ERROR_FAILED_REMOVING_AUDITOR_ROLE = "Failed removing Product Function Team {0} for user {1}.";
    public static final String ERROR_FAILED_AUDITOR_ROLE_REMOVE = "Failed invalid removing Product Function Team.";
    public static final String ERROR_FAILED_BACK_UP = "Failed backing up information.";
    public static final String ERROR_FAILED_IA_DIVISION_REMOVE = "Failed invalid removing Lead Product Team";
    public static final String ERROR_FAILED_REMOVING_IA_DIVISION = "Failed removing ia_division {0} for user {1}.";
    public static final String ERROR_INVALID_ROLE_TEMPLATE_IA_DIVISION = "Failed to assign Lead Product Team.";
    public static final String ERROR_FAILED_ADDING_ROLE_ASSIGNMENT_IA_DIVISION = "Failed assigning Lead Product Team {0} for user {1}.";
    public static final String ERROR_FAILED_USER_NOT_BELONG_GROUP = "Failed removing role {0} for user {1}. The user is not member of that role.";
    public static final String ERROR_USER_IS_NOT_LEVEL_AUDITOR_ROLE_ALLOWED = "Failed to assign Product Function Team. The group {0} is not member of {1}.";
}
