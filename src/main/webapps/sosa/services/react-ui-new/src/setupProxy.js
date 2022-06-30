const {createProxyMiddleware} = require('http-proxy-middleware');

const proxy = 'http://opservflin1811.fyre.ibm.com:10108/openpages'
const newProxy = 'http://opservflin1811.fyre.ibm.com:10108'

const apiProxy = createProxyMiddleware({
    target: newProxy,
    changeOrigin: true,
    secure: false,
    logLevel: 'debug'
});

const authProxy = createProxyMiddleware({
    target: proxy,
    changeOrigin: true,
    secure: false,
    proxyTimeout: 5 * 60 * 1000,
    logLevel: 'debug',
    onProxyRes: (proxyRes, req, res) => {
        if (proxyRes.statusCode === 302) { // 302 is logon success (redirect to home page)
            proxyRes.statusCode = 200;
        }
    },
    onProxyReq: (proxyReq, req, res) => {
        if (proxyReq.path && proxyReq.path.includes('/j_security_check')) {
            // clear cookie 'LtpaToken2' for re-login.
            let cookie = proxyReq.getHeader('cookie');
            if (cookie) {
                cookie = cookie.replace(/OPLtpaToken2([^;]*)(;\s)?/, '')
                proxyReq.setHeader('cookie', cookie);
            }

        } else if (proxyReq.path && proxyReq.path.includes('/singlesignon.do')) {
            // clear cookie 'JSESSIONID' for re-login.
            let cookie = proxyReq.getHeader('cookie');
            if (cookie) {
                cookie = cookie.replace(/OPJSESSIONID([^;]*)(;\s)?/, '')
                proxyReq.setHeader('cookie', cookie);
            }
        }
    },
});

module.exports = function (app) {
    app.use('/api', apiProxy);
    app.use('/openpages/app/services', apiProxy);
    app.use('/report', apiProxy);
    app.use('/app/jspview/helper/index.html', authProxy);
    app.use('/app/services/helperapp/citi-helpers/index.html', apiProxy);
    app.use('/app/jspview/react/index.html', authProxy);
    app.use('/log.off.do', authProxy);
    app.use('/j_security_check', authProxy);
    app.use('/singlesignon.do', authProxy);
};

