package exceptions

class SameGuidException(guid: String) :
    RuntimeException("Guid $guid already exists")