function NukeDB {
    entityframeworkcore\drop-database -project ssm\ssm.entity
    entityframeworkcore\update-database -project ssm\ssm.entity
}
