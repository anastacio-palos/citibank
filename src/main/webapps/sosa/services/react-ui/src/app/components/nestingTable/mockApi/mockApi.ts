import {IRecord} from "../../../stores/generalDetailsAccordionStore/generalDetailsAccordionStore";
import {ITableProps} from "../../../stores/nestingTable/nestingTableDataStore";
import {IHeaderInfo} from "../../header/CustomHeader";
import mockResponse from './mockResponse.json';

export enum HelperNames {
    auditHelper = 'auditHelper',
    newDsmtLinks = 'newDsmtLinks'
}

export class MockApi {
    private static instance: MockApi;

    private constructor() {
    }

    static getInstance(): MockApi {
        if (!MockApi.instance) {
            MockApi.instance = new MockApi();
        }
        return MockApi.instance;
    }

    static renameKeys = (keysMap: any, obj: any) => {
        return Object
            .keys(obj)
            .reduce((acc, key) => {
                const renamedObject = {
                    [keysMap[key] || key]: obj[key]
                };
                return {
                    ...acc,
                    ...renamedObject
                }
            }, {});
    };

// TODO: Correct the type info in these functions
    async getTableData(): Promise<ITableProps> {
        const response = mockResponse.auditHelper.existingDSMTLinksGridInfo;

        const keysMap = {
            expanded: 'isExpanded',
            selected: 'isSelected'
        };

        const newRows: any = response.rows.map((row) => {
            return MockApi.renameKeys(keysMap, row);
        })

        const updatedResponse: ITableProps = {
            ...response,
            rows: [
                ...newRows
            ]
        };

        return updatedResponse;
    }

    async getGeneralDetailsData(): Promise<IRecord[]> {
        return mockResponse.auditHelper.helperObjectContentInfo.generalDetails;
    }

    async getCustomHeaderData(helperName: HelperNames): Promise<IHeaderInfo> {
        return mockResponse[helperName].headerInfo
    }
}
