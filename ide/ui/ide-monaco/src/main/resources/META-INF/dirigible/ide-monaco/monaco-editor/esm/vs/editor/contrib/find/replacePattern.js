/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2021 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
import { buildReplaceStringWithCasePreserved } from '../../../base/common/search.js';
/**
 * Assigned when the replace pattern is entirely static.
 */
var StaticValueReplacePattern = /** @class */ (function () {
    function StaticValueReplacePattern(staticValue) {
        this.staticValue = staticValue;
        this.kind = 0 /* StaticValue */;
    }
    return StaticValueReplacePattern;
}());
/**
 * Assigned when the replace pattern has replacemend patterns.
 */
var DynamicPiecesReplacePattern = /** @class */ (function () {
    function DynamicPiecesReplacePattern(pieces) {
        this.pieces = pieces;
        this.kind = 1 /* DynamicPieces */;
    }
    return DynamicPiecesReplacePattern;
}());
var ReplacePattern = /** @class */ (function () {
    function ReplacePattern(pieces) {
        if (!pieces || pieces.length === 0) {
            this._state = new StaticValueReplacePattern('');
        }
        else if (pieces.length === 1 && pieces[0].staticValue !== null) {
            this._state = new StaticValueReplacePattern(pieces[0].staticValue);
        }
        else {
            this._state = new DynamicPiecesReplacePattern(pieces);
        }
    }
    ReplacePattern.fromStaticValue = function (value) {
        return new ReplacePattern([ReplacePiece.staticValue(value)]);
    };
    Object.defineProperty(ReplacePattern.prototype, "hasReplacementPatterns", {
        get: function () {
            return (this._state.kind === 1 /* DynamicPieces */);
        },
        enumerable: true,
        configurable: true
    });
    ReplacePattern.prototype.buildReplaceString = function (matches, preserveCase) {
        if (this._state.kind === 0 /* StaticValue */) {
            if (preserveCase) {
                return buildReplaceStringWithCasePreserved(matches, this._state.staticValue);
            }
            else {
                return this._state.staticValue;
            }
        }
        var result = '';
        for (var i = 0, len = this._state.pieces.length; i < len; i++) {
            var piece = this._state.pieces[i];
            if (piece.staticValue !== null) {
                // static value ReplacePiece
                result += piece.staticValue;
                continue;
            }
            // match index ReplacePiece
            result += ReplacePattern._substitute(piece.matchIndex, matches);
        }
        return result;
    };
    ReplacePattern._substitute = function (matchIndex, matches) {
        if (matches === null) {
            return '';
        }
        if (matchIndex === 0) {
            return matches[0];
        }
        var remainder = '';
        while (matchIndex > 0) {
            if (matchIndex < matches.length) {
                // A match can be undefined
                var match = (matches[matchIndex] || '');
                return match + remainder;
            }
            remainder = String(matchIndex % 10) + remainder;
            matchIndex = Math.floor(matchIndex / 10);
        }
        return '$' + remainder;
    };
    return ReplacePattern;
}());
export { ReplacePattern };
/**
 * A replace piece can either be a static string or an index to a specific match.
 */
var ReplacePiece = /** @class */ (function () {
    function ReplacePiece(staticValue, matchIndex) {
        this.staticValue = staticValue;
        this.matchIndex = matchIndex;
    }
    ReplacePiece.staticValue = function (value) {
        return new ReplacePiece(value, -1);
    };
    ReplacePiece.matchIndex = function (index) {
        return new ReplacePiece(null, index);
    };
    return ReplacePiece;
}());
export { ReplacePiece };
var ReplacePieceBuilder = /** @class */ (function () {
    function ReplacePieceBuilder(source) {
        this._source = source;
        this._lastCharIndex = 0;
        this._result = [];
        this._resultLen = 0;
        this._currentStaticPiece = '';
    }
    ReplacePieceBuilder.prototype.emitUnchanged = function (toCharIndex) {
        this._emitStatic(this._source.substring(this._lastCharIndex, toCharIndex));
        this._lastCharIndex = toCharIndex;
    };
    ReplacePieceBuilder.prototype.emitStatic = function (value, toCharIndex) {
        this._emitStatic(value);
        this._lastCharIndex = toCharIndex;
    };
    ReplacePieceBuilder.prototype._emitStatic = function (value) {
        if (value.length === 0) {
            return;
        }
        this._currentStaticPiece += value;
    };
    ReplacePieceBuilder.prototype.emitMatchIndex = function (index, toCharIndex) {
        if (this._currentStaticPiece.length !== 0) {
            this._result[this._resultLen++] = ReplacePiece.staticValue(this._currentStaticPiece);
            this._currentStaticPiece = '';
        }
        this._result[this._resultLen++] = ReplacePiece.matchIndex(index);
        this._lastCharIndex = toCharIndex;
    };
    ReplacePieceBuilder.prototype.finalize = function () {
        this.emitUnchanged(this._source.length);
        if (this._currentStaticPiece.length !== 0) {
            this._result[this._resultLen++] = ReplacePiece.staticValue(this._currentStaticPiece);
            this._currentStaticPiece = '';
        }
        return new ReplacePattern(this._result);
    };
    return ReplacePieceBuilder;
}());
/**
 * \n			=> inserts a LF
 * \t			=> inserts a TAB
 * \\			=> inserts a "\".
 * $$			=> inserts a "$".
 * $& and $0	=> inserts the matched substring.
 * $n			=> Where n is a non-negative integer lesser than 100, inserts the nth parenthesized submatch string
 * everything else stays untouched
 *
 * Also see https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/replace#Specifying_a_string_as_a_parameter
 */
export function parseReplaceString(replaceString) {
    if (!replaceString || replaceString.length === 0) {
        return new ReplacePattern(null);
    }
    var result = new ReplacePieceBuilder(replaceString);
    for (var i = 0, len = replaceString.length; i < len; i++) {
        var chCode = replaceString.charCodeAt(i);
        if (chCode === 92 /* Backslash */) {
            // move to next char
            i++;
            if (i >= len) {
                // string ends with a \
                break;
            }
            var nextChCode = replaceString.charCodeAt(i);
            // let replaceWithCharacter: string | null = null;
            switch (nextChCode) {
                case 92 /* Backslash */:
                    // \\ => inserts a "\"
                    result.emitUnchanged(i - 1);
                    result.emitStatic('\\', i + 1);
                    break;
                case 110 /* n */:
                    // \n => inserts a LF
                    result.emitUnchanged(i - 1);
                    result.emitStatic('\n', i + 1);
                    break;
                case 116 /* t */:
                    // \t => inserts a TAB
                    result.emitUnchanged(i - 1);
                    result.emitStatic('\t', i + 1);
                    break;
            }
            continue;
        }
        if (chCode === 36 /* DollarSign */) {
            // move to next char
            i++;
            if (i >= len) {
                // string ends with a $
                break;
            }
            var nextChCode = replaceString.charCodeAt(i);
            if (nextChCode === 36 /* DollarSign */) {
                // $$ => inserts a "$"
                result.emitUnchanged(i - 1);
                result.emitStatic('$', i + 1);
                continue;
            }
            if (nextChCode === 48 /* Digit0 */ || nextChCode === 38 /* Ampersand */) {
                // $& and $0 => inserts the matched substring.
                result.emitUnchanged(i - 1);
                result.emitMatchIndex(0, i + 1);
                continue;
            }
            if (49 /* Digit1 */ <= nextChCode && nextChCode <= 57 /* Digit9 */) {
                // $n
                var matchIndex = nextChCode - 48 /* Digit0 */;
                // peek next char to probe for $nn
                if (i + 1 < len) {
                    var nextNextChCode = replaceString.charCodeAt(i + 1);
                    if (48 /* Digit0 */ <= nextNextChCode && nextNextChCode <= 57 /* Digit9 */) {
                        // $nn
                        // move to next char
                        i++;
                        matchIndex = matchIndex * 10 + (nextNextChCode - 48 /* Digit0 */);
                        result.emitUnchanged(i - 2);
                        result.emitMatchIndex(matchIndex, i + 1);
                        continue;
                    }
                }
                result.emitUnchanged(i - 1);
                result.emitMatchIndex(matchIndex, i + 1);
                continue;
            }
        }
    }
    return result.finalize();
}