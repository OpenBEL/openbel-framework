" Vim syntax file for BEL
" Language: BEL (http://www.belframework.org)
" Maintainer: Anthony Bargnesi
" Revision: 1
" Date: July 1, 2011

" exit if a syntax has already defined, if so exit.
if exists("b:current_syntax")
  finish
endif

" match patterns
syn match object_ident   '\w\+'
syn match quoted_value   '"\_.\{-}"'
syn match equals         '='
syn match colon          ':'
syn match left_curly     '{'
syn match right_curly    '}'
syn match left_paren     '('
syn match right_paren    ')'
syn match colon          ':'
syn match comments       '^#.*'

" recognize common keywords
syn keyword ReservedKeywords SET DEFINE UNSET DOCUMENT ANNOTATION NAMESPACE AS URL PATTERN LIST STATEMENT_GROUP DEFAULT

let b:current_syntax = "bel"

hi equals           ctermfg=LightGray
hi colon            ctermfg=LightGray
hi left_curly       ctermfg=LightGray
hi right_curly      ctermfg=LightGray
hi left_paren       ctermfg=LightGray
hi right_paren      ctermfg=LightGray
hi colon            ctermfg=LightGray
hi object_ident     ctermfg=LightCyan
hi quoted_value     ctermfg=Red
hi comments         ctermfg=DarkBlue
hi ReservedKeywords ctermfg=DarkYellow
