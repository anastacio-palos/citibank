import axios from 'axios'

class AuditEntityHelperService {

    constructor() {
        this.config = {}
    }

    retrieveHeaderData(helperName) {


        return axios.get('/openpages/app/jspview/auditScopingApp/helper/' + helperName + '/header', this.config)

    }

    retrieveAuditableEntityTree(id) {

        return axios.get('/api/v1/tree/ae', this.config)
    }

    retrieveAuditableEntity(id) {

        return axios.get('/openpages/app/jspview/auditScopingApp/aes/audit/' + id, this.config)
    }

    retrieveAuditData(id) {

        // axios.get('/openpages/app/jspview/audEntityDsmtLinkApp/init?resourceId=2251', this.config).then(response => {

        //     console.log("--- first response setting the param");
        // })
        // let search = {
        //     isTreeSearch : false,
        //     searchText: '%cat%',
        //     nodeSelected: 'MS',
        //     manageSegNodeId: '',
        //     legalVehicleNodeId: '',
        //     manageGeographyNodeId: ''
        // }
        // axios.post('/openpages/app/jspview/audEntityDsmtLinkApp/searchDSMT/22518',search, this.config).then(response => {
        //     console.log("---------- response from service" + JSON.stringify(response.data))
        //     //this.setState({aes:response.data})
        //   });

        return axios.get('/openpages/app/jspview/auditScopingApp/' + id, this.config)
    }

    saveData(auditID, rejectionData) {
        console.log(auditID, rejectionData)
        return axios.post('/openpages/app/jspview/auditScopingApp/audit/' + auditID, rejectionData, this.config)
    }

}

export default new AuditEntityHelperService();