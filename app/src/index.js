import * as odca from "odca";
import JSZip from "jszip";

document.querySelector("#chooseFile")
    .addEventListener("change", (e) => {
        const file = e.target.files[0];
        parseFile(file).then((results) => {
            const obj = new odca.com.thehumancolossuslab.odca.Deserializer(JSON.stringify(results[6])).call()
            console.log(JSON.parse(obj))
        })
    });

const parseFile = async (file) => {
    const jszip = new JSZip()
    const results = []
    const promises = []
    let schemaNames

    await jszip.loadAsync(file).then((zip) => {
        schemaNames = Object.values(zip.files)
            .filter((f) => f.dir)
            .map((dir) => dir.name.replace("/", ""))

        schemaNames.forEach((name) => {
            const p = []

            p.push({
                name: "schemaBase",
                value: zip.file(`${name}.json`).async("string")
            })
            let overlayNames = Object.values(zip.files)
                .filter((f) => !f.dir && f.name.match(`${name}/`))
                .map((overlay) => overlay.name)
            
            overlayNames.forEach((overlayName) => {
                p.push({
                    name: overlayName.replace(`${name}/`, ""),
                    value: zip.file(overlayName).async("string")
                })
            })

            promises.push(p)

        })
    })
    await Promise.all(promises.map((schemaPromise) => {
        return Promise.all(schemaPromise.map(({value}) => value)).then((schema)=> {
            let result = {}
            schema.forEach((content, i) => {
                result[schemaPromise[i]["name"]] = JSON.parse(content)
            })
            results.push(result)
        })
    }))

    return results;
}