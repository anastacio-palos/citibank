import {makeAutoObservable} from 'mobx';
import {IAboutInfo, IHeaderInfo, IUserInfo} from '../../components/header/CustomHeader';
import {HelperNames, MockApi} from '../../components/nestingTable/mockApi/mockApi';

export class CustomHeaderDataStore {
    api: MockApi;
    loading: boolean;
    headerTitle: string;
    userInfo: IUserInfo;
    aboutInfo: IAboutInfo;
    hideSearch: boolean;
    hideNotificationBell: boolean;
    hideAppSwitcher: boolean;
    hideUserIcon: boolean;

    constructor(helperName?: HelperNames) {
        this.api = MockApi.getInstance();
        this.loading = false;
        this.headerTitle = 'DEFAULT';
        this.userInfo = {
            displayName: 'DEFAULT'
        };
        this.hideSearch = true;
        this.hideAppSwitcher = true;
        this.hideNotificationBell = true;
        this.hideUserIcon = false;
        this.aboutInfo = {
            title: 'DEFAULT HELPER',
            version: '1.0',
            licenseInfo: 'DEFAULT',
            helperInfo: 'DEFAULT'
        }
        makeAutoObservable(this);
        this.load(helperName);
    }

    private load(helperName: HelperNames = HelperNames.auditHelper) {
        this.loading = true;
        this.api.getCustomHeaderData(helperName)
            .then(response => this.onLoad(response))
            .catch(error => console.log(`ERROR ${error}`));
    }

    private onLoad(helperData: IHeaderInfo) {
        const {
            headerTitle,
            userInfo,
            aboutInfo,
            hideSearch,
            hideAppSwitcher,
            hideNotificationBell,
            hideUserIcon
        } = helperData;

        this.loading = false;
        this.headerTitle = headerTitle;
        this.userInfo = userInfo;
        this.aboutInfo = aboutInfo;
        this.hideSearch = hideSearch;
        this.hideAppSwitcher = hideAppSwitcher;
        this.hideNotificationBell = hideNotificationBell;
        this.hideUserIcon = hideUserIcon;
    }
}
