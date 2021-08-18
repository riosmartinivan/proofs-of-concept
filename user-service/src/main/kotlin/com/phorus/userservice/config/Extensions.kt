package com.phorus.userservice.config

import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.javaType
import kotlin.reflect.jvm.jvmErasure

inline fun <T: Any, reified B, reified C> Any.mapTo(
    entityClass: KClass<out T>,
    exclusions: List<String>? = null,
    mappings: HashMap<String, String>? = null,
    customMappings: HashMap<String, Pair<String, (source: B) -> C>>? = null,
): T {
    val entity: T = entityClass.createInstance()

    this.javaClass.kotlin.memberProperties.forEach {
        entity::class.memberProperties.forEach targetLoop@ { prop ->
            if (prop is KMutableProperty<*>) {
                if (exclusions?.contains(prop.name) == true) return@targetLoop
                val targetPropType = prop.returnType.toString().replace("?", "")
                val originalPropType = it.returnType.toString().replace("?", "")
                val originalProp = it.get(this) ?: return@targetLoop

                // If the props have the same name and type, or if the fields have the
                //  same type and are customMappings, then map them
                if (prop.name == it.name || (mappings?.contains(it.name) == true && prop.name == mappings[it.name])) {

                    if (originalPropType == targetPropType) {
                        prop.setter.call(entity, originalProp)
                    } else { // If they are not from the same type, try to map them as embedded objects
                        prop.setter.call(entity, originalProp.mapTo(prop.returnType.jvmErasure))
                    }
                }

                // If custom mappings are provided, and if one of the custom mappings uses
                //  the curren originalProp as source
                if (customMappings?.contains(it.name) == true) {

                    // And if the target prop is found
                    if (prop.name == customMappings[it.name]?.first) {
                        // The source of the data must be the same type as the input of the function
                        if (originalPropType != B::class.qualifiedName.toString()
                                .replace("?", "")) return@targetLoop

                        // The return value cannot be null
                        val newValue = customMappings[it.name]?.second!!(originalProp as B) ?: return@targetLoop

                        // The return value of the function must be the same type as the targetProp
                        if (targetPropType != newValue::class.qualifiedName.toString()
                                .replace("?", "")) return@targetLoop

                        prop.setter.call(entity, newValue)
                    }
                }
            }
        }
    }

    return entity
}

fun <T: Any> Any.mapTo(
    entityClass: KClass<out T>,
    exclusions: List<String>? = null,
    mappings: HashMap<String, String>? = null,
): T {
    val entity: T = entityClass.createInstance()

    this.javaClass.kotlin.memberProperties.forEach {
        entity::class.memberProperties.forEach targetLoop@{ prop ->
            if (prop is KMutableProperty<*>) {
                if (exclusions?.contains(prop.name) == true) return@targetLoop
                val targetPropType = prop.returnType.toString().replace("?", "")
                val originalPropType = it.returnType.toString().replace("?", "")
                val originalProp = it.get(this) ?: return@targetLoop

                // If the props have the same name and type, or if the fields have the
                //  same type and are customMappings, then map them
                if (prop.name == it.name || (mappings?.contains(it.name) == true && prop.name == mappings[it.name])) {

                    if (originalPropType == targetPropType) {
                        prop.setter.call(entity, originalProp)
                    } else { // If they are not from the same type, try to map them as embedded objects
                        prop.setter.call(entity, originalProp.mapTo(prop.returnType.jvmErasure))
                    }
                }
            }
        }
    }

    return entity
}