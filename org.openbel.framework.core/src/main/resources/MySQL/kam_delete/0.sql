ALTER TABLE @.statement_annotation_map
	DROP FOREIGN KEY a_sa_fk
##
ALTER TABLE @.document_annotation_def_map
	DROP FOREIGN KEY ad_dad_fk
##
ALTER TABLE @.annotation
	DROP FOREIGN KEY ad_a_fk
##
ALTER TABLE @.annotation_definition
	DROP FOREIGN KEY ad_adt_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY d_s_fk
##
ALTER TABLE @.document_namespace_map
	DROP FOREIGN KEY d_dn_fk
##
ALTER TABLE @.document_annotation_def_map
	DROP FOREIGN KEY d_dad_fk
##
ALTER TABLE @.kam_node
	DROP FOREIGN KEY ft_kn_fk
##
ALTER TABLE @.kam_edge_statement_map
    DROP FOREIGN KEY ke_s_stmt_fk
##
ALTER TABLE @.kam_edge_statement_map
    DROP FOREIGN KEY ke_s_ke_fk
##
ALTER TABLE @.kam_node_parameter
	DROP FOREIGN KEY kn_knp_fk
##
ALTER TABLE @.kam_edge
	DROP FOREIGN KEY kn_kes_fk
##
ALTER TABLE @.kam_edge
	DROP FOREIGN KEY kn_ket_fk
##
ALTER TABLE @.term
	DROP FOREIGN KEY kn_t_fk
##
ALTER TABLE @.document_namespace_map
	DROP FOREIGN KEY n_dn_fk
##
ALTER TABLE @.term_parameter
	DROP FOREIGN KEY n_p_fk
##
ALTER TABLE @.annotation
	DROP FOREIGN KEY o_a_n_fk
##
ALTER TABLE @.annotation_definition
	DROP FOREIGN KEY o_ad_fk
##
ALTER TABLE @.term_parameter
	DROP FOREIGN KEY o_p_fk
##
ALTER TABLE @.namespace
	DROP FOREIGN KEY o_n_fk
##
ALTER TABLE @.objects
	DROP FOREIGN KEY otype_o_fk
##
ALTER TABLE @.kam_edge
	DROP FOREIGN KEY rt_ke_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY rt_s_fk
##
ALTER TABLE @.statement_annotation_map
	DROP FOREIGN KEY s_sa_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY kn_tst_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY kn_tot_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY kn_ns_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY nrt_s_fk
##
ALTER TABLE @.statement
	DROP FOREIGN KEY kn_no_fk
##
ALTER TABLE @.term_parameter
	DROP FOREIGN KEY t_tp_fk
##
DROP TABLE @.annotation
##
DROP TABLE @.annotation_definition
##
DROP TABLE @.annotation_definition_type
##
DROP TABLE @.document_header_information
##
DROP TABLE @.document_annotation_def_map
##
DROP TABLE @.document_namespace_map
##
DROP TABLE @.function_type
##
DROP TABLE @.kam_edge_statement_map
##
DROP TABLE @.kam_edge
##
DROP TABLE @.kam_node
##
DROP TABLE @.kam_node_parameter
##
DROP TABLE @.namespace
##
DROP TABLE @.objects
##
DROP TABLE @.objects_text
##
DROP TABLE @.objects_type
##
DROP TABLE @.relationship_type
##
DROP TABLE @.statement
##
DROP TABLE @.statement_annotation_map
##
DROP TABLE @.term
##
DROP TABLE @.term_parameter
##