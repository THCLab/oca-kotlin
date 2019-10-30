import * as odca from "odca";
import JSZip from "jszip";

document.querySelector("#chooseFile")
    .addEventListener("change", (e) => {
        const file = e.target.files[0];
        const jszip = new JSZip()
        const result = {}

        jszip.loadAsync(file).then((zip) => {
            const schemaNames = Object.values(zip.files)
                .filter((f) => f.dir)
                .map((dir) => dir.name.replace("/", ""))

            schemaNames.forEach((name) => {
                zip.file(`${name}.json`).async("string").then((r) => {
                    result[name] = {
                        "schemaBase": JSON.parse(r)
                    }
                })

                let overlayNames = Object.values(zip.files)
                    .filter((f) => !f.dir && f.name.match(`${name}/`))
                    .map((overlay) => overlay.name)
                
                overlayNames.forEach((overlayName) => {
                    zip.file(overlayName).async("string").then((r) => {
                        result[name][overlayName.replace(`${name}/`, "")] = JSON.parse(r)
                    })
                })
            })
        })

        const obj = new odca.com.thehumancolossuslab.odca.AClass(result)
        console.log(obj.schemaBase())
    });