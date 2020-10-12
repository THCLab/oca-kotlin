package com.thehumancolossuslab.oca

object Hello {
    @JvmStatic
    fun main(args : Array<String>) {
      val schemasArray = listOf(
      hashMapOf("schemaBase" to """{
          "@context": "https://odca.tech/v1",
          "name": "AuditOverview",
          "type": "spec/schema_base/1.0",
          "description": "A schema to be used by audit contractors and suppliers to capture general audit report information, including the contact details of all audit participants",
          "classification": "GICS:35202010",
          "issued_by": "",
          "attributes": {
            "auditReportNumber": "Text", "auditReportOwner": "Text", "auditDate": "Date", "auditType": "Text", "auditTypeSpecify": "Text", "previousAuditDate": "Date", "previousAuditType": "Text", "previousAuditTypeSpecify": "Text", "auditFirmName": "Text", "leadAuditorName": "Text", "leadAuditorJobTitle": "Text", "furtherAuditorNames": "Array[Text]", "furtherAuditorJobTitles": "Array[Text]", "leadAuditorPhoneNumber": "Text", "leadAuditorEmail": "Text", "companyName": "Text", "siteName": "Text", "parentCompanyName": "Text", "siteAddress": "Text", "siteCity": "Text", "siteState": "Text", "sitePostalCode": "Text", "siteCountry": "Text", "siteRepresentativeName": "Text", "siteRepresentativeJobTitle": "Text", "siteRepresentativePhoneNumber": "Text", "siteRepresentativeEmailAddress": "Text", "managementRepresentativeName": "Text", "managementRepresentativeJobTitle": "Text", "hseRepresentativeName": "Text", "hseRepresentativeJobTitle": "Text", "hrRepresentativeName": "Text", "hrRepresentativeJobTitle": "Text", "findingClassificationMethod": "Text", "workersAttitude": "Text", "managementAttitude": "Text", "keyFindingsSummary": "Text"
          },
          "pii_attributes": [
            "auditReportNumber", "auditReportOwner", "auditDate", "auditTypeSpecify", "previousAuditDate", "previousAuditTypeSpecify", "auditFirmName", "leadAuditorName", "leadAuditorJobTitle", "furtherAuditorNames", "furtherAuditorJobTitles", "leadAuditorPhoneNumber", "leadAuditorEmail", "companyName", "siteName", "parentCompanyName", "siteAddress", "sitePostalCode", "siteRepresentativeName", "siteRepresentativeJobTitle", "siteRepresentativePhoneNumber", "siteRepresentativeEmailAddress", "managementRepresentativeName", "managementRepresentativeJobTitle", "hseRepresentativeName", "hseRepresentativeJobTitle", "hrRepresentativeName", "hrRepresentativeJobTitle", "keyFindingsSummary"
          ]
        }""",
      "LabelOverlay-hl:dMDmaoE4iejcsvNypLj1E5Wf4eejapmdXNgffrHdAVeG.json" to """{
              "@context": "https://odca.tech/overlays/v1",
              "type": "spec/overlay/label/1.0",
              "description": "Category and attribute labels for AuditOverview",
              "issued_by": "",
              "role": "",
              "purpose": "",
              "schema_base": "hl:i2GRPzKQMxhGE4TM3kVqg3gYt3Wgt5agRLs16PEG2DSf",
              "language": "en_US",
              "attr_labels": {
                "auditReportNumber": "Audit report number:", "auditReportOwner": "Report owner:", "auditDate": "Date of audit:", "auditType": "Type of audit:", "auditTypeSpecify": "If other, please specify:", "previousAuditDate": "Date of previous audit:", "previousAuditType": "Type of previous audit:", "previousAuditTypeSpecify": "If other, please specify:", "auditFirmName": "Audit firm name:", "leadAuditorName": "Lead auditor name:", "leadAuditorJobTitle": "Lead auditor job title:", "furtherAuditorNames": "Names of further auditors:", "furtherAuditorJobTitles": "Further auditors’ job titles:", "leadAuditorPhoneNumber": "Phone number:", "leadAuditorEmail": "Email:", "companyName": "Company name", "siteName": "Site name", "parentCompanyName": "Parent company name", "siteAddress": "Site street address", "siteCity": "Site city", "siteState": "Site state/province/county", "sitePostalCode": "Site postal/zip code", "siteCountry": "Site country", "siteRepresentativeName": "Site representative name (host)", "siteRepresentativeJobTitle": "Job title", "siteRepresentativePhoneNumber": "Phone number", "siteRepresentativeEmailAddress": "Email address", "managementRepresentativeName": "Management representative name", "managementRepresentativeJobTitle": "Job title", "hseRepresentativeName": "HSE (Health, Safety, Environment) representative name", "hseRepresentativeJobTitle": "Job title", "hrRepresentativeName": "HR (Human Resources) representative name", "hrRepresentativeJobTitle": "Job title", "findingClassificationMethod": "Finding classification method", "workersAttitude": "Attitude of workers (towards management, workplace, and the interview process)", "managementAttitude": "Attitude of management (to audit and audit process)", "keyFindingsSummary": "Summary of key findings"
              },
              "attr_categories": [
                "auditor_and_audit_report_information", "facility_details", "site_contact_information", "executive_summary"
              ],
              "category_labels": {
                "auditor_and_audit_report_information": "Auditor and Audit Report Information", "facility_details": "Facility Details", "site_contact_information": "Site Contact Information", "executive_summary": "Executive summary"
              },
              "category_attributes": {
                "auditor_and_audit_report_information": [
                  "auditReportNumber", "auditReportOwner", "auditDate", "auditType", "auditTypeSpecify", "previousAuditDate", "previousAuditType", "previousAuditTypeSpecify", "auditFirmName", "leadAuditorName", "leadAuditorJobTitle", "furtherAuditorNames", "furtherAuditorJobTitles", "leadAuditorPhoneNumber", "leadAuditorEmail"
                ],
                "facility_details": [
                  "companyName", "siteName", "parentCompanyName", "siteAddress", "siteCity", "siteState", "sitePostalCode", "siteCountry"
                ],
                "site_contact_information": [
                  "siteRepresentativeName", "siteRepresentativeJobTitle", "siteRepresentativePhoneNumber", "siteRepresentativeEmailAddress", "managementRepresentativeName", "managementRepresentativeJobTitle", "hseRepresentativeName", "hseRepresentativeJobTitle", "hrRepresentativeName", "hrRepresentativeJobTitle"
                ],
                "executive_summary": [
                  "findingClassificationMethod"
                ]
              }
            }""",
        "FormatOverlay-hl:2yvBBHiSE24pBdBFAThAcQq9konLSGkDnPynSbZjq1TZ.json" to """{
          "@context": "https://odca.tech/overlays/v1",
          "type": "spec/overlay/format/1.0",
          "description": "Attribute formats for AuditOverview",
          "issued_by": "",
          "role": "",
          "purpose": "",
          "schema_base": "hl:i2GRPzKQMxhGE4TM3kVqg3gYt3Wgt5agRLs16PEG2DSf",
          "attr_formats": {
            "auditDate": "DD/MM/YYYY", "previousAuditDate": "DD/MM/YYYY"
          }
        }""")).toTypedArray()

        var facade = Facade()
        val schemas = facade.deserializeSchemas(schemasArray)
        val schema = schemas.first()

        println(schema.schemaBase)
    }
}