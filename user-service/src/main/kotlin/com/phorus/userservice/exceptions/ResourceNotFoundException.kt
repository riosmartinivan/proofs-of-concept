package com.phorus.userservice.exceptions

import java.lang.RuntimeException

class ResourceNotFoundException(message: String?) : RuntimeException(message)