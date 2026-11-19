package com.alexriley.me.hhSignTeleport.permissions

import org.bukkit.permissions.Permissible
import org.bukkit.permissions.PermissionDefault

enum class HHSTPermission(
    val node: String,
    val description: String,
    val default: PermissionDefault = PermissionDefault.OP,
    val parent: HHSTPermission? = null
) {
    // Wildcard
    ADMIN("hhst.admin", "Grants all HHST permissions", PermissionDefault.OP),

    // Basic Commands
    SIGN_CREATE("hhst.create", "Allows creating HHST warp signs", PermissionDefault.TRUE),
    SIGN_USE("hhst.use", "Allows basic HHST sign usage", PermissionDefault.TRUE),
    SIGN_EDIT("hhst.edit", "Allows editing own HHST signs", PermissionDefault.TRUE),
    // SIGN_EDIT_OTHERS("hhst.editOthers", "Allows editing others HHST signs", PermissionDefault.OP),
    // SIGN_REMOVE_OTHERS("hhst.removeOthers", "Allows removing others HHST signs", PermissionDefault.OP),
    SIGN_REMOVE("hhst.remove", "Allows removing own HHST signs", PermissionDefault.TRUE);

    fun has(permissible: Permissible): Boolean = permissible.hasPermission(node);
}

