export const pagination = {
	_page: 1,
	rowsPerPage: 10,
	descending: true,
	sortBy: 'createdAt',
}

export const opt = {
	total: 0,
	list: [],
	obj: {},
	_sort: {},
	_match: {},
	_search: {},
	_select: '',
	_in: {},
	_nin: {},
	selected: [],
	...pagination,
	isLoading: false,
	isCreate: false,
	isUpdate: false,
	isDelete: false,
	isForm: false,
	pages: [10, 20, 50, 100],
}