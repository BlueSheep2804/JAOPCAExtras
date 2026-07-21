plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.1"

stonecutter.parameters {
    constants["forge"] = current.parsed < "1.21"
    constants["neoforge"] = current.parsed >= "1.21"
}
