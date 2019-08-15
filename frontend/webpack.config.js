
var path = require('path');

module.exports = {
    entry: './src/index.js',
    output: {
        path: __dirname,
        filename: './src/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets:  ['@babel/preset-env',
                            '@babel/react',{
                                'plugins': ['@babel/plugin-proposal-class-properties']}]
                        ,
                        babelrcRoots: ['.', '../']
                    }
                }]
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(png|woff|woff2|eot|ttf|svg)$/,
                use: 'url-loader?limit=100000'
            }
        ]
    }

};
