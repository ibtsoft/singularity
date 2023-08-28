rootProject.name = "singularity"

enableFeaturePreview("VERSION_CATALOGS")

include("singularity-core")
include("singularity-security")
include("singularity-persistence-mongodb")
include("singularity-web")
include("singularity-search")
