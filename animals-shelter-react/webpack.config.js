const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const webpack = require('webpack');

module.exports = {
    entry: './src/index.js',
    devtool: 'source-map',
    cache: true,
    mode: 'development',
    output: {
        path: path.resolve(__dirname, 'build'),
        filename: 'bundle.js',
        publicPath: '/',
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
            {
                test: /\.(png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot|otf|json)$/,
                type: 'asset/resource',
                generator: {
                    filename: 'static/[hash][ext][query]',
                }
            }
        ]
    },
    resolve: {
        extensions: ['.js', '.jsx'],
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, 'public/index.html'),
            filename: 'index.html',
            inject: 'body',
        }),
        new CopyWebpackPlugin({
                    patterns: [
                        { from: 'public/img', to: 'static/img' }
                    ]
                })
    ],
    devServer: {
        port: 3000,
        static: path.join(__dirname, 'public'),
        hot: true,
        open: true,
        historyApiFallback: true,
        host: '0.0.0.0',
        allowedHosts: 'all',
    }
};
